// =============================================================================
// send-push-notification
//
// Supabase Edge Function — sends a Firebase Cloud Messaging (FCM) push
// notification to a single device token.
//
// Invoked by Postgres database webhooks (via pg_net / Supabase webhooks) when:
//   • a notification row is inserted into public.notifications
//
// Also called directly by the chore-deadline pg_cron job via a helper
// function that inserts notification rows (which then trigger this webhook).
//
// Environment variables required (set via `supabase secrets set`):
//   FIREBASE_SERVICE_ACCOUNT  — JSON string of the Firebase service account key
//
// Request body (JSON):
//   {
//     "type": "INSERT",
//     "table": "notifications",
//     "record": {
//       "id": "...",
//       "recipient_id": "...",
//       "household_id": "...",
//       "type": "chore_assigned" | "chore_deadline" | "family_member_added" | ...,
//       "payload": { ... },
//       "is_read": false,
//       "created_at": "..."
//     }
//   }
// =============================================================================

import { createClient } from "jsr:@supabase/supabase-js@2";

// ---------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------

interface NotificationRecord {
  id: string;
  recipient_id: string;
  household_id: string;
  type: string;
  payload: Record<string, unknown>;
  is_read: boolean;
  created_at: string;
}

interface WebhookPayload {
  type: "INSERT" | "UPDATE" | "DELETE";
  table: string;
  record: NotificationRecord;
  schema: string;
  old_record: NotificationRecord | null;
}

interface FcmMessage {
  message: {
    token: string;
    notification: {
      title: string;
      body: string;
    };
    data?: Record<string, string>;
    android?: {
      priority: "normal" | "high";
    };
  };
}

// ---------------------------------------------------------------------------
// Notification title/body builders
// ---------------------------------------------------------------------------

function buildNotificationContent(
  type: string,
  payload: Record<string, unknown>
): { title: string; body: string } {
  switch (type) {
    case "chore_assigned":
      return {
        title: "New chore assigned",
        body: `You've been assigned: ${payload.chore_title ?? "a new chore"}`,
      };
    case "chore_deadline":
      return {
        title: "Chore due today",
        body: `Don't forget: ${payload.chore_title ?? "a chore"} is due today`,
      };
    case "family_member_added":
      return {
        title: "Welcome to the household!",
        body: `You've been added to ${payload.household_name ?? "a household"}`,
      };
    case "chore_completed":
      return {
        title: "Chore completed",
        body: `${payload.completed_by ?? "Someone"} completed: ${payload.chore_title ?? "a chore"}`,
      };
    case "chore_edited":
      return {
        title: "Chore updated",
        body: `${payload.chore_title ?? "A chore"} has been updated`,
      };
    case "reward_request":
      return {
        title: "Reward request",
        body: `${payload.requester_name ?? "Someone"} is requesting a reward`,
      };
    default:
      return {
        title: "Churros",
        body: "You have a new notification",
      };
  }
}

// ---------------------------------------------------------------------------
// Google OAuth2 — service account JWT for FCM v1
// ---------------------------------------------------------------------------

/**
 * Signs a JWT using the RS256 algorithm with the service account private key.
 * Deno's built-in Web Crypto API is used — no third-party JWT library needed.
 */
async function signJwt(
  payload: Record<string, unknown>,
  privateKeyPem: string
): Promise<string> {
  // Strip PEM armor and decode base64 to DER bytes
  const pemBody = privateKeyPem
    .replace(/-----BEGIN PRIVATE KEY-----/, "")
    .replace(/-----END PRIVATE KEY-----/, "")
    .replace(/\s+/g, "");

  const keyBytes = Uint8Array.from(atob(pemBody), (c) => c.charCodeAt(0));

  const cryptoKey = await crypto.subtle.importKey(
    "pkcs8",
    keyBytes,
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    false,
    ["sign"]
  );

  const header = { alg: "RS256", typ: "JWT" };
  const encodeBase64Url = (obj: unknown) =>
    btoa(JSON.stringify(obj))
      .replace(/\+/g, "-")
      .replace(/\//g, "_")
      .replace(/=+$/, "");

  const headerB64 = encodeBase64Url(header);
  const payloadB64 = encodeBase64Url(payload);
  const signingInput = `${headerB64}.${payloadB64}`;

  const signature = await crypto.subtle.sign(
    "RSASSA-PKCS1-v1_5",
    cryptoKey,
    new TextEncoder().encode(signingInput)
  );

  const sigB64 = btoa(String.fromCharCode(...new Uint8Array(signature)))
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");

  return `${signingInput}.${sigB64}`;
}

/**
 * Obtains a short-lived Google OAuth2 access token for the FCM v1 scope,
 * using the service account credentials.
 */
async function getAccessToken(serviceAccount: {
  client_email: string;
  private_key: string;
}): Promise<string> {
  const now = Math.floor(Date.now() / 1000);
  const jwtPayload = {
    iss: serviceAccount.client_email,
    sub: serviceAccount.client_email,
    aud: "https://oauth2.googleapis.com/token",
    iat: now,
    exp: now + 3600,
    scope: "https://www.googleapis.com/auth/firebase.messaging",
  };

  const signedJwt = await signJwt(jwtPayload, serviceAccount.private_key);

  const tokenResponse = await fetch("https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams({
      grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
      assertion: signedJwt,
    }),
  });

  if (!tokenResponse.ok) {
    const err = await tokenResponse.text();
    throw new Error(`Failed to obtain Google access token: ${err}`);
  }

  const tokenJson = await tokenResponse.json();
  return tokenJson.access_token as string;
}

// ---------------------------------------------------------------------------
// FCM v1 — send message
// ---------------------------------------------------------------------------

async function sendFcmMessage(
  fcmMessage: FcmMessage,
  projectId: string,
  accessToken: string
): Promise<void> {
  const url = `https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`;

  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify(fcmMessage),
  });

  if (!response.ok) {
    const err = await response.text();
    throw new Error(`FCM send failed (${response.status}): ${err}`);
  }
}

// ---------------------------------------------------------------------------
// Handler
// ---------------------------------------------------------------------------

Deno.serve(async (req: Request) => {
  try {
    // Only accept POST requests from the Supabase webhook
    if (req.method !== "POST") {
      return new Response("Method not allowed", { status: 405 });
    }

    // Parse the webhook payload
    const webhookPayload: WebhookPayload = await req.json();

    // Only handle INSERT events on the notifications table
    if (
      webhookPayload.type !== "INSERT" ||
      webhookPayload.table !== "notifications"
    ) {
      return new Response("Ignored", { status: 200 });
    }

    const notification = webhookPayload.record;

    // ---------------------------------------------------------------------------
    // Look up the recipient's FCM push token from the profiles table
    // Use service-role client to bypass RLS (this runs as a service worker)
    // ---------------------------------------------------------------------------

    const supabaseUrl = Deno.env.get("SUPABASE_URL");
    const supabaseServiceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");

    if (!supabaseUrl || !supabaseServiceRoleKey) {
      console.error("Missing SUPABASE_URL or SUPABASE_SERVICE_ROLE_KEY");
      return new Response("Server configuration error", { status: 500 });
    }

    const supabase = createClient(supabaseUrl, supabaseServiceRoleKey);

    const { data: profile, error: profileError } = await supabase
      .from("profiles")
      .select("push_token")
      .eq("id", notification.recipient_id)
      .single();

    if (profileError || !profile) {
      console.error(
        "Failed to fetch recipient profile:",
        profileError?.message
      );
      return new Response("Recipient not found", { status: 404 });
    }

    const pushToken = profile.push_token as string | null;

    // No push token — recipient hasn't registered a device; skip silently
    if (!pushToken) {
      console.log(
        `Recipient ${notification.recipient_id} has no push token — skipping`
      );
      return new Response("No push token", { status: 200 });
    }

    // ---------------------------------------------------------------------------
    // Load the Firebase service account key from secrets
    // ---------------------------------------------------------------------------

    const serviceAccountJson = Deno.env.get("FIREBASE_SERVICE_ACCOUNT");
    if (!serviceAccountJson) {
      console.error("Missing FIREBASE_SERVICE_ACCOUNT secret");
      return new Response("Server configuration error", { status: 500 });
    }

    const serviceAccount = JSON.parse(serviceAccountJson) as {
      project_id: string;
      client_email: string;
      private_key: string;
    };

    // ---------------------------------------------------------------------------
    // Build the FCM message
    // ---------------------------------------------------------------------------

    const { title, body } = buildNotificationContent(
      notification.type,
      notification.payload
    );

    const fcmMessage: FcmMessage = {
      message: {
        token: pushToken,
        notification: { title, body },
        // Forward structured payload as string-valued data extras for the
        // Android client to deep-link into the right screen.
        data: {
          notification_id: notification.id,
          notification_type: notification.type,
          household_id: notification.household_id,
          // Stringify the payload so the client can parse it at the other end
          payload: JSON.stringify(notification.payload),
        },
        android: {
          // Use high priority so notifications arrive immediately
          priority: "high",
        },
      },
    };

    // ---------------------------------------------------------------------------
    // Authenticate with Google and send the FCM message
    // ---------------------------------------------------------------------------

    const accessToken = await getAccessToken(serviceAccount);
    await sendFcmMessage(fcmMessage, serviceAccount.project_id, accessToken);

    console.log(
      `Push notification sent to ${notification.recipient_id} (type: ${notification.type})`
    );

    return new Response(JSON.stringify({ success: true }), {
      status: 200,
      headers: { "Content-Type": "application/json" },
    });
  } catch (err) {
    console.error("Unexpected error in send-push-notification:", err);
    return new Response(
      JSON.stringify({
        error: err instanceof Error ? err.message : String(err),
      }),
      { status: 500, headers: { "Content-Type": "application/json" } }
    );
  }
});
