-- =============================================================================
-- notifications
-- Activity feed entries delivered to a specific household member.
-- Inserted by service-role edge functions; never by clients directly.
-- =============================================================================

create type if not exists public.notification_type as enum (
    'chore_assigned',
    'chore_completed',
    'reward_request',
    'daily_goal'
);

create table if not exists public.notifications (
    id            uuid                      primary key default gen_random_uuid(),
    recipient_id  uuid                      not null references public.profiles (id) on delete cascade,
    household_id  uuid                      not null references public.households (id) on delete cascade,
    type          public.notification_type  not null,
    payload       jsonb                     not null default '{}',   -- context-specific data (chore title, requester name, …)
    is_read       boolean                   not null default false,
    created_at    timestamptz               not null default now()
);

-- Fetch all notifications for a recipient efficiently.
create index if not exists notifications_recipient_id_idx
    on public.notifications (recipient_id);

-- Support household-scoped queries (e.g. admin views).
create index if not exists notifications_household_id_idx
    on public.notifications (household_id);

-- ---------------------------------------------------------------------------
-- TTL — purge notifications older than 1 month
-- pg_cron job to schedule (run once in the Supabase SQL editor or via CLI):
--
--   select cron.schedule(
--       'purge-old-notifications',
--       '0 3 * * *',
--       $$ delete from public.notifications where created_at < now() - interval '1 month' $$
--   );
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- Row Level Security
-- ---------------------------------------------------------------------------

alter table public.notifications enable row level security;

-- A user can read their own notifications.
create policy "notifications: recipient can select"
    on public.notifications
    for select
    using (auth.uid() = recipient_id);

-- A user can mark their own notifications as read (update is_read only).
create policy "notifications: recipient can update"
    on public.notifications
    for update
    using (auth.uid() = recipient_id)
    with check (auth.uid() = recipient_id);

-- A user can delete their own notifications.
create policy "notifications: recipient can delete"
    on public.notifications
    for delete
    using (auth.uid() = recipient_id);

-- Inserts are handled only by service-role edge functions.
-- No direct insert policy is granted to authenticated users.
