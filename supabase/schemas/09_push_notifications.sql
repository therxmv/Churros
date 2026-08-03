-- =============================================================================
-- push notifications
--
-- Extends the notification system with:
--   1. New enum values for push-only notification types.
--   2. Postgres trigger: chore assigned → INSERT into notifications.
--   3. Postgres trigger: household member added → INSERT into notifications.
--   4. pg_cron job: daily check for chores due today → INSERT notifications.
--   5. Supabase webhook wiring comment (manual step in the dashboard).
--
-- The send-push-notification Edge Function is invoked via a Supabase Database
-- Webhook that fires on INSERT into public.notifications.  The Edge Function
-- reads the new row, fetches the recipient's FCM token from public.profiles,
-- and calls the FCM v1 HTTP API.
--
-- Prerequisites:
--   07_notifications.sql — notifications table + notification_type enum
--   06_chores.sql        — chores table
--   04_household_members.sql — household_members table + helper functions
--   03_households.sql    — households table
--   02_profiles.sql      — profiles table (push_token column)
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. Extend notification_type enum
--    Add values needed by the new triggers.
--    `if not exists` is not supported by ALTER TYPE ADD VALUE — use DO blocks
--    with a catalogue check so this file is idempotent.
-- ---------------------------------------------------------------------------

do $$
begin
    -- chore_deadline: a chore's due date is today (inserted by pg_cron job)
    if not exists (
        select 1 from pg_enum
        where enumlabel = 'chore_deadline'
          and enumtypid = 'public.notification_type'::regtype
    ) then
        alter type public.notification_type add value 'chore_deadline';
    end if;
end;
$$;

do $$
begin
    -- family_member_added: a user has been added to a household by a parent
    if not exists (
        select 1 from pg_enum
        where enumlabel = 'family_member_added'
          and enumtypid = 'public.notification_type'::regtype
    ) then
        alter type public.notification_type add value 'family_member_added';
    end if;
end;
$$;

-- ---------------------------------------------------------------------------
-- 2. Trigger function: notify assignee when a chore is assigned
--
-- Fires AFTER INSERT or UPDATE on public.chores.
-- A notification is inserted when:
--   • A new chore row is created with a non-null assignee_id, OR
--   • An existing chore's assignee_id changes to a new (non-null) value.
--
-- The payload carries enough context for the Edge Function to build a
-- human-readable push body without an extra DB round-trip.
-- ---------------------------------------------------------------------------

create or replace function public.notify_chore_assigned()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
declare
    v_household_name text;
begin
    -- Only proceed when there is a (new) assignee
    if new.assignee_id is null then
        return new;
    end if;

    -- On UPDATE: skip if the assignee has not changed
    if tg_op = 'UPDATE' and old.assignee_id is not distinct from new.assignee_id then
        return new;
    end if;

    -- Look up the household name for the notification payload
    select name into v_household_name
    from public.households
    where id = new.household_id;

    insert into public.notifications (
        recipient_id,
        household_id,
        type,
        payload
    ) values (
        new.assignee_id,
        new.household_id,
        'chore_assigned',
        jsonb_build_object(
            'chore_id',        new.id,
            'chore_title',     new.title,
            'household_name',  v_household_name,
            'due_at',          new.due_at,
            'priority',        new.priority
        )
    );

    return new;
end;
$$;

-- Drop and recreate so the definition stays in sync with declarative resets.
drop trigger if exists on_chore_assigned on public.chores;

create trigger on_chore_assigned
    after insert or update of assignee_id on public.chores
    for each row
    execute procedure public.notify_chore_assigned();

-- ---------------------------------------------------------------------------
-- 3. Trigger function: notify new member when they are added to a household
--
-- Fires AFTER INSERT on public.household_members.
-- Sends a 'family_member_added' notification to the newly added user so they
-- know they've been included in a household.
-- ---------------------------------------------------------------------------

create or replace function public.notify_family_member_added()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
declare
    v_household_name text;
begin
    -- Look up the household name for the notification payload
    select name into v_household_name
    from public.households
    where id = new.household_id;

    insert into public.notifications (
        recipient_id,
        household_id,
        type,
        payload
    ) values (
        new.user_id,
        new.household_id,
        'family_member_added',
        jsonb_build_object(
            'household_id',   new.household_id,
            'household_name', v_household_name,
            'role',           new.role
        )
    );

    return new;
end;
$$;

-- Drop and recreate so the definition stays in sync with declarative resets.
drop trigger if exists on_household_member_added on public.household_members;

create trigger on_household_member_added
    after insert on public.household_members
    for each row
    execute procedure public.notify_family_member_added();

-- ---------------------------------------------------------------------------
-- 4. pg_cron — chore deadline reminder (daily, runs at 08:00 UTC)
--
-- Finds all incomplete chores whose due_at falls on the current calendar day
-- (in UTC) and inserts a 'chore_deadline' notification for each assignee,
-- provided they haven't already received one today (idempotency guard).
--
-- Run this once via the Supabase SQL editor or CLI after schema is applied.
-- pg_cron persists the job in the cron schema — it only needs to be
-- scheduled once per environment.
--
-- Prerequisites: pg_cron extension must be enabled in the Supabase project
-- (Dashboard → Database → Extensions → pg_cron).
-- ---------------------------------------------------------------------------

-- Schedule the daily deadline-reminder job.
-- The DO block makes this idempotent: it unschedules any existing job with
-- the same name before re-creating it so re-running this file is safe.
do $$
begin
    -- Remove existing job (cron.unschedule is a no-op if the name is unknown)
    perform cron.unschedule('chore-deadline-reminders');
exception
    when undefined_function then
        raise notice 'pg_cron not available — skipping chore-deadline-reminders job scheduling';
        return;
    when others then
        null; -- job did not exist yet — that is fine
end;
$$;

do $$
begin
    perform cron.schedule(
        'chore-deadline-reminders',
        '0 8 * * *',  -- every day at 08:00 UTC
        $cron$
            insert into public.notifications (recipient_id, household_id, type, payload)
            select
                c.assignee_id,
                c.household_id,
                'chore_deadline',
                jsonb_build_object(
                    'chore_id',    c.id,
                    'chore_title', c.title,
                    'due_at',      c.due_at,
                    'priority',    c.priority
                )
            from public.chores c
            where
                -- Chore is due today (UTC calendar day)
                c.due_at::date = current_date
                -- Chore has an assignee
                and c.assignee_id is not null
                -- Chore is not yet completed
                and c.completed_at is null
                -- Idempotency guard: don't send a second reminder if one was
                -- already inserted today (e.g. if the job is re-triggered)
                and not exists (
                    select 1 from public.notifications n
                    where n.recipient_id  = c.assignee_id
                      and n.household_id  = c.household_id
                      and n.type          = 'chore_deadline'
                      and n.payload ->> 'chore_id' = c.id::text
                      and n.created_at::date = current_date
                );
        $cron$
    );
exception
    when undefined_function then
        raise notice 'pg_cron not available — skipping chore-deadline-reminders job scheduling';
    when others then
        raise;
end;
$$;

-- ---------------------------------------------------------------------------
-- 5. Supabase Database Webhook — wiring the Edge Function
--
-- A Supabase Database Webhook must be created in the dashboard (or via the
-- Supabase Management API) to invoke the send-push-notification Edge Function
-- whenever a row is inserted into public.notifications.
--
-- Dashboard path:
--   Database → Webhooks → Create a new hook
--
-- Settings:
--   Name:         notify-on-insert
--   Table:        public.notifications
--   Events:       INSERT
--   Type:         Supabase Edge Functions
--   Edge Function: send-push-notification
--   HTTP headers: (leave as default — the service-role JWT is added automatically)
--
-- Alternatively, create it via the Management API:
--
--   curl -X POST https://api.supabase.com/v1/projects/<project-ref>/database/webhooks \
--     -H "Authorization: Bearer <management-api-key>" \
--     -H "Content-Type: application/json" \
--     -d '{
--       "name": "notify-on-insert",
--       "schema": "public",
--       "table": "notifications",
--       "events": ["INSERT"],
--       "function_name": "send-push-notification",
--       "function_type": "edge_function"
--     }'
--
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- 6. Supabase secret — Firebase service account key
--
-- Store the Firebase service account JSON as a Supabase secret so the Edge
-- Function can read it via Deno.env.get("FIREBASE_SERVICE_ACCOUNT").
--
-- Run once per environment (never commit the key to source control):
--
--   supabase secrets set FIREBASE_SERVICE_ACCOUNT="$(cat path/to/firebase-service-account.json)"
--
-- Verify the secret is set:
--
--   supabase secrets list
--
-- The service account must have the "Firebase Cloud Messaging API" enabled in
-- the Google Cloud Console for your Firebase project and the
-- cloudmessaging.messages.create IAM permission (pre-packaged in the
-- "Firebase Cloud Messaging Admin SDK Service Agent" role).
-- ---------------------------------------------------------------------------
