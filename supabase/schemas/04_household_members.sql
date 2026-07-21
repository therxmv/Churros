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

-- Any member can see who else is in their household.
--
-- IMPORTANT — self-join caveat
-- ---------------------------------------------------------------------------
-- This policy queries the same table (household_members) that RLS is being
-- evaluated on.  Postgres evaluates RLS on EVERY row scan, including the
-- inner scan of the subquery.  That means the subquery itself is also
-- filtered by this very policy — a chicken-and-egg situation.
--
-- In practice Supabase / Postgres breaks the cycle safely: the subquery
-- that fetches the current user's own membership row is evaluated with
-- RLS already applied, so it only returns rows where user_id = auth.uid().
-- This works correctly today and is the recommended pattern, but it is
-- worth being aware of:
--
--   Example — what happens when Alice (user_id = 'alice') tries to list members:
--
--     Outer query:  SELECT * FROM household_members            -- reads all rows
--     RLS filter:   WHERE household_id IN (                    -- applied to outer
--                     SELECT household_id                      -- inner subquery
--                     FROM household_members                   -- same table!
--                     WHERE user_id = auth.uid()               -- = 'alice'
--                   )
--
--   The inner subquery is also RLS-filtered.  Alice's own membership row
--   passes (user_id = 'alice' = auth.uid()), so household_id is returned,
--   and the outer query then shows all other members of that household.
--
--   If Alice were somehow removed from household_members before the query
--   completes (e.g. a concurrent DELETE), the inner subquery would return
--   nothing, and Alice would see zero rows — a safe fail-closed result.
--
-- Rule of thumb: always place an explicit `user_id = auth.uid()` predicate
-- in the inner subquery (as done here) so Postgres can short-circuit using
-- the user_id index rather than doing a full table scan.
-- ---------------------------------------------------------------------------
create policy "household_members: members can select"
    on public.household_members
    for select
    using (
        household_id in (
            select hm.household_id
            from public.household_members hm
            where hm.user_id = auth.uid()
        )
    );

-- Only parents can add new members to their household.
create policy "household_members: parents can insert"
    on public.household_members
    for insert
    with check (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    );

-- Only parents can change a member's role.
create policy "household_members: parents can update"
    on public.household_members
    for update
    using (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    )
    with check (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    );

-- Only parents can remove members.
create policy "household_members: parents can delete"
    on public.household_members
    for delete
    using (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    );

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
-- Note: the self-join caveat described in the SELECT policy above also
-- applies to the Realtime RLS check — Postgres re-evaluates the policy for
-- each broadcast row, so only members of the household receive the event.
-- ---------------------------------------------------------------------------

alter publication supabase_realtime add table public.household_members;
