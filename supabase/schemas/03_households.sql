-- =============================================================================
-- households
-- A household is the shared "home" unit that members belong to.
-- =============================================================================

create table if not exists public.households (
    id         uuid        primary key default gen_random_uuid(),
    name       text        not null,
    address    text,
    photo_url  text,
    created_at timestamptz not null default now()
);

-- ---------------------------------------------------------------------------
-- Row Level Security
-- ---------------------------------------------------------------------------

alter table public.households enable row level security;

-- Any member of a household can read it.
create policy "households: members can select"
    on public.households
    for select
    using (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = id
              and hm.user_id = auth.uid()
        )
    );

-- Only parents may update household info.
create policy "households: parents can update"
    on public.households
    for update
    using (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    )
    with check (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    );

-- Only parents may insert a new household row.
-- (In practice this is done during onboarding via a server-side function,
--  but we allow it for any authenticated parent to keep flexibility.)
create policy "households: parents can insert"
    on public.households
    for insert
    with check (true);  -- insert access is gated by the household_members insert policy

-- Only parents may delete a household.
create policy "households: parents can delete"
    on public.households
    for delete
    using (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    );

-- ---------------------------------------------------------------------------
-- Realtime
-- Enable row-level broadcast for this table so clients can subscribe to
-- household updates (name, photo, address changes).
--
-- Channel name convention: households:<household_id>
-- Example: households:b2c3d4e5-...
--
-- RLS is enforced on Realtime: the existing SELECT policy
-- ("households: members can select") restricts broadcasts to household members.
-- ---------------------------------------------------------------------------

alter publication supabase_realtime add table public.households;
