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
