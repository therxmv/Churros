# Auth Setup Checklist

Manual steps that cannot be automated via `config.toml` or code.
Complete these once per environment (staging / production).

---

## 1. Google OAuth credentials

1. Open [Google Cloud Console](https://console.cloud.google.com/) and select (or create) the Churros project.
2. Navigate to **APIs & Services → Credentials → Create Credentials → OAuth 2.0 Client ID**.
3. Choose application type **Android** for the native client and **Web application** for the Supabase server-side client.
4. For the **Web application** client, add the Supabase callback URL as an authorised redirect URI:
   ```
   https://<project-ref>.supabase.co/auth/v1/callback
   ```
5. Copy the **Client ID** and **Client Secret** from the web application client.
6. Set them as environment variables (or Supabase dashboard secrets):
   ```
   GOOGLE_CLIENT_ID=<web-client-id>
   GOOGLE_CLIENT_SECRET=<web-client-secret>
   ```
   These map to `env(GOOGLE_CLIENT_ID)` / `env(GOOGLE_CLIENT_SECRET)` in `config.toml`.
7. For local development, add the variables to a `.env` file at the repo root (never commit it).

---

## 2. Supabase dashboard — Google provider

1. Go to **Authentication → Providers → Google** in the Supabase dashboard.
2. Toggle **Enable Google provider** on.
3. Paste the Client ID and Client Secret from step 1.
4. Confirm the authorised redirect URI shown matches what you added in Google Cloud Console.

---

## 3. Email templates

1. Go to **Authentication → Email Templates** in the Supabase dashboard.
2. Review and customise:
   - **Confirm signup** — sent when `enable_confirmations = true` (currently enabled).
   - **Change email address** — sent when `double_confirm_changes = true` (currently enabled).
   - **Reset password** — sent on password-reset requests.
3. Update the sender name and reply-to address under **Authentication → Settings → SMTP**.

---

## 4. Allowed redirect URLs (production)

Ensure the following URLs are listed under **Authentication → URL Configuration → Redirect URLs** in the dashboard for every environment:

- `com.therxmv.churros://auth/callback` — Android OAuth deep-link (already in `config.toml` for local).
- `https://<your-production-domain>` — if a web client is ever added.

---

## 5. Apple OAuth (future)

Apple Sign-In requires certificates that cannot be stored in `config.toml`. When ready:

1. Enroll in the [Apple Developer Program](https://developer.apple.com/).
2. Create a **Services ID** for the app and configure Sign in with Apple.
3. Generate a private key and note the Key ID and Team ID.
4. Enable the Apple provider in the Supabase dashboard (**Authentication → Providers → Apple**) and fill in all four fields: Client ID (Services ID), Secret Key, Key ID, Team ID.
5. Add `com.therxmv.churros://auth/callback` as a return URL in the Apple Services ID configuration.
6. Add an `[auth.external.apple]` block to `config.toml` following the same pattern as Google.

---

## 6. Local development smoke test

```bash
# Start local Supabase stack
supabase start

# Confirm the Google provider appears in local Auth settings
open http://127.0.0.1:54323   # Supabase Studio → Authentication → Providers
```

Launch the Android app on an emulator, trigger Google Sign-In, and verify the deep-link
`com.therxmv.churros://auth/callback` is received by `MainActivity`.
