-- =============================================================================
-- household_members
-- Join table linking users to households with a role.
-- UNIQUE(user_id) enforces one household per user.
-- =============================================================================

create table if not exists public.household_members (
    user_id      uuid              not null references public.profiles (id) on delete cascade,
    household_id uuid              not null references public.households (id) on delete cascade,
    role         public.household_role not null default 'parent',
    joined_at    timestamptz       not null default now(),

    primary key (user_id, household_id),

    -- One household per user — a user cannot belong to multiple households.
    constraint household_members_user_id_unique unique (user_id)
);

create index if not exists household_members_household_id_idx
    on public.household_members (household_id);

-- ---------------------------------------------------------------------------
-- Row Level Security
-- ---------------------------------------------------------------------------

alter table public.household_members enable row level security;

-- ---------------------------------------------------------------------------
-- Helper functions (SECURITY DEFINER — bypass RLS for membership checks)
-- Using plain subqueries in policies would create self-referential RLS on
-- this table. These functions run as the function owner, sidestepping the
-- cycle cleanly.
-- ---------------------------------------------------------------------------

create or replace function public.get_my_household_ids()
returns setof uuid
language sql security definer stable
set search_path = public
as $$
    select household_id from public.household_members where user_id = auth.uid();
$$;

create or replace function public.is_household_member(p_household_id uuid)
returns boolean
language sql security definer stable
set search_path = public
as $$
    select exists (
        select 1 from public.household_members
        where household_id = p_household_id and user_id = auth.uid()
    );
$$;

create or replace function public.is_household_parent(p_household_id uuid)
returns boolean
language sql security definer stable
set search_path = public
as $$
    select exists (
        select 1 from public.household_members
        where household_id = p_household_id
          and user_id = auth.uid()
          and role = 'parent'
    );
$$;

-- Any member can see who else is in their household.
create policy "household_members: members can select"
    on public.household_members
    for select
    using (household_id in (select public.get_my_household_ids()));

-- Only parents can add new members to their household.
create policy "household_members: parents can insert"
    on public.household_members
    for insert
    with check (public.is_household_parent(household_id));

-- Only parents can change a member's role.
create policy "household_members: parents can update"
    on public.household_members
    for update
    using (public.is_household_parent(household_id))
    with check (public.is_household_parent(household_id));

-- Only parents can remove members.
create policy "household_members: parents can delete"
    on public.household_members
    for delete
    using (public.is_household_parent(household_id));

-- ---------------------------------------------------------------------------
-- Realtime
-- Enable row-level broadcast for this table so clients can react when a new
-- member joins, a role changes, or a member is removed.
--
-- Channel name convention: household_members:<household_id>
-- Example: household_members:b2c3d4e5-...
--
-- RLS is enforced on Realtime: the existing SELECT policy
-- ("household_members: members can select") restricts broadcasts to members
-- of the same household.
--
-- RLS is enforced on Realtime: the helper functions above are re-evaluated
-- per broadcast row, so only household members receive events.
-- ---------------------------------------------------------------------------

alter publication supabase_realtime add table public.household_members;
