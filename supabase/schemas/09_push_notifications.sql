-- =============================================================================
-- push notifications
--
-- Extends the notification system with Postgres triggers that insert rows into
-- public.notifications, which are then picked up by the Supabase Database
-- Webhook → send-push-notification Edge Function pipeline.
--
-- Triggers:
--   1. notify_chore_assigned — fires when a chore's assignee_id is set
--   2. notify_chore_edited   — fires when chore fields are updated
--
-- Prerequisites:
--   07_notifications.sql     — notifications table + notification_type enum
--   06_chores.sql            — chores table
--   03_households.sql        — households table
--   02_profiles.sql          — profiles table (push_token column)
-- =============================================================================

-- ---------------------------------------------------------------------------
-- 1. Trigger function: notify assignee when a chore is assigned
--
-- Fires AFTER INSERT or UPDATE OF assignee_id on public.chores.
-- A notification is inserted when:
--   • A new chore row is created with a non-null assignee_id, OR
--   • An existing chore's assignee_id changes to a new (non-null) value.
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
    if new.assignee_id is null then
        return new;
    end if;

    -- On UPDATE: skip if assignee hasn't changed
    if tg_op = 'UPDATE' and old.assignee_id is not distinct from new.assignee_id then
        return new;
    end if;

    select name into v_household_name
    from public.households
    where id = new.household_id;

    insert into public.notifications (recipient_id, household_id, type, payload)
    values (
        new.assignee_id,
        new.household_id,
        'chore_assigned',
        jsonb_build_object(
            'chore_id',       new.id,
            'chore_title',    new.title,
            'household_name', v_household_name,
            'due_at',         new.due_at,
            'priority',       new.priority
        )
    );

    return new;
end;
$$;

drop trigger if exists on_chore_assigned on public.chores;

create trigger on_chore_assigned
    after insert or update of assignee_id on public.chores
    for each row
    execute procedure public.notify_chore_assigned();

-- ---------------------------------------------------------------------------
-- 2. Trigger function: notify assignee when a chore is edited
--
-- Fires AFTER UPDATE on public.chores when meaningful fields change.
-- Only fires when the chore has an assignee (nothing to notify otherwise).
-- Does not fire when only assignee_id changes — that is handled above.
-- ---------------------------------------------------------------------------

create or replace function public.notify_chore_edited()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
    -- Nothing to notify if there is no assignee
    if new.assignee_id is null then
        return new;
    end if;

    -- Skip if no meaningful field changed
    if (
        new.title        is not distinct from old.title        and
        new.description  is not distinct from old.description  and
        new.due_at       is not distinct from old.due_at       and
        new.priority     is not distinct from old.priority
    ) then
        return new;
    end if;

    insert into public.notifications (recipient_id, household_id, type, payload)
    values (
        new.assignee_id,
        new.household_id,
        'chore_edited',
        jsonb_build_object(
            'chore_id',    new.id,
            'chore_title', new.title,
            'due_at',      new.due_at,
            'priority',    new.priority
        )
    );

    return new;
end;
$$;

drop trigger if exists on_chore_edited on public.chores;

create trigger on_chore_edited
    after update on public.chores
    for each row
    execute procedure public.notify_chore_edited();

-- ---------------------------------------------------------------------------
-- 3. Supabase Database Webhook — wiring the Edge Function
--
-- Dashboard path: Database → Webhooks → Create a new hook
--
-- Settings:
--   Name:          notify-on-insert
--   Table:         public.notifications
--   Events:        INSERT
--   Type:          Supabase Edge Functions
--   Edge Function: send-push-notification
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- 4. Supabase secret — Firebase service account key
--
-- Run once per environment (never commit the key to source control):
--
--   supabase secrets set FIREBASE_SERVICE_ACCOUNT="$(cat path/to/firebase-service-account.json)"
--
-- The service account must have the "Firebase Cloud Messaging Admin SDK Service
-- Agent" IAM role (or cloudmessaging.messages.create permission).
-- ---------------------------------------------------------------------------
