-- =============================================================================
-- profiles
-- One row per authenticated user. Mirrors auth.users (1-to-1).
-- =============================================================================

create table if not exists public.profiles (
    id          uuid        primary key references auth.users (id) on delete cascade,
    display_name text        not null,
    avatar_url  text,
    push_token  text,                        -- FCM token; updated by client on login/refresh
    created_at  timestamptz not null default now()
);

-- Let PostgREST / realtime resolve rows by user id efficiently.
create index if not exists profiles_id_idx on public.profiles (id);

-- ---------------------------------------------------------------------------
-- Row Level Security
-- ---------------------------------------------------------------------------

alter table public.profiles enable row level security;

-- A user can read their own profile.
create policy "profiles: owner can select"
    on public.profiles
    for select
    using (auth.uid() = id);

-- A user can update their own profile.
create policy "profiles: owner can update"
    on public.profiles
    for update
    using (auth.uid() = id)
    with check (auth.uid() = id);

-- Inserts are handled only by the handle_new_user trigger (security definer).
-- No direct insert policy is granted to authenticated users.
