-- =============================================================================
-- chores
-- A chore is a task that belongs to a household and may be assigned to a member.
-- =============================================================================

create type if not exists public.chore_priority as enum ('low', 'medium', 'high');

create table if not exists public.chores (
    id              uuid             primary key default gen_random_uuid(),
    household_id    uuid             not null references public.households (id) on delete cascade,
    created_by      uuid             references public.profiles (id) on delete set null,
    assignee_id     uuid             references public.profiles (id) on delete set null,
    title           text             not null,
    category        varchar,                              -- free-text label (e.g. "cleaning", "cooking")
    priority        chore_priority   not null default 'medium',
    reward_points   integer          not null default 0 check (reward_points >= 0),
    due_at          timestamptz,
    repeat_schedule text,                                 -- nullable RRULE or simple value (daily/weekly/monthly)
    completed_at    timestamptz,                          -- null = not completed; set when marked done
    created_at      timestamptz      not null default now()
);

-- Fetch all chores for a household efficiently.
create index if not exists chores_household_id_idx
    on public.chores (household_id);

-- Look up chores assigned to a specific user quickly.
create index if not exists chores_assignee_id_idx
    on public.chores (assignee_id);

-- ---------------------------------------------------------------------------
-- Row Level Security
-- ---------------------------------------------------------------------------

alter table public.chores enable row level security;

-- Any member of the household can read its chores.
create policy "chores: members can select"
    on public.chores
    for select
    using (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
        )
    );

-- Any member of the household can create a chore.
create policy "chores: members can insert"
    on public.chores
    for insert
    with check (
        exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
        )
    );

-- Only the assignee or a parent of the household may update a chore.
create policy "chores: assignee or parent can update"
    on public.chores
    for update
    using (
        auth.uid() = assignee_id
        or exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    )
    with check (
        auth.uid() = assignee_id
        or exists (
            select 1
            from public.household_members hm
            where hm.household_id = household_id
              and hm.user_id = auth.uid()
              and hm.role = 'parent'
        )
    );

-- Only parents may delete a chore.
create policy "chores: parents can delete"
    on public.chores
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
-- Enable row-level broadcast for this table so clients can subscribe to
-- chore changes (new assignments, completions, edits, deletions).
--
-- Channel name convention: chores:<household_id>
-- Example: chores:b2c3d4e5-...
--
-- RLS is enforced on Realtime: the existing SELECT policy
-- ("chores: members can select") restricts broadcasts to members of the
-- relevant household.
-- ---------------------------------------------------------------------------

alter publication supabase_realtime add table public.chores;
