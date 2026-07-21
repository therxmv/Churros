# Supabase — Churros Backend

This directory contains everything needed to stand up and maintain the Churros Supabase project: schema definitions, auth configuration notes, and this overview.

---

## Contents

```
supabase/
├── config.toml          — Local dev stack configuration (auth providers, ports, etc.)
├── AUTH_SETUP.md        — Manual steps for Google OAuth, email templates, redirect URLs
├── README.md            — This file
└── schemas/             — SQL schema files applied in numbered order
    ├── 01_types.sql
    ├── 02_profiles.sql
    ├── 03_households.sql
    ├── 04_household_members.sql
    ├── 05_triggers.sql
    ├── 06_chores.sql
    └── 07_notifications.sql
```

---

## Local Development

```bash
# Start the full local Supabase stack (Postgres, Studio, Auth, Realtime, Storage)
supabase start

# Apply all schema files in order
for f in supabase/schemas/*.sql; do psql "$DATABASE_URL" -f "$f"; done

# Open Supabase Studio
open http://127.0.0.1:54323

# Stop the stack
supabase stop
```

The local `DATABASE_URL` is printed by `supabase start`. It looks like:
```
postgresql://postgres:postgres@127.0.0.1:54322/postgres
```

---

## Schema Numbering Convention

Schema files are prefixed with a two-digit number so they can be applied in a deterministic order:

| File | What it defines |
|------|-----------------|
| `01_types.sql` | Shared enums (`household_role`, etc.) |
| `02_profiles.sql` | `profiles` table — one row per auth user |
| `03_households.sql` | `households` table |
| `04_household_members.sql` | Join table linking users to households with roles |
| `05_triggers.sql` | `handle_new_user` trigger — creates a profile on sign-up |
| `06_chores.sql` | `chores` table and `chore_priority` enum |
| `07_notifications.sql` | `notifications` table and `notification_type` enum |

**Rule:** later files may reference earlier ones (e.g. `chores` FK → `households`), but never the reverse.  When adding a new table, pick the next available number.

---

## Authentication

Auth is handled by Supabase Auth with Google OAuth as the primary provider.  Email/password is also enabled.

All manual setup steps that cannot be automated via `config.toml` are documented in [AUTH_SETUP.md](AUTH_SETUP.md):

- Creating Google OAuth credentials in Google Cloud Console
- Enabling the Google provider in the Supabase dashboard
- Customising email templates
- Configuring the Android deep-link redirect URL (`com.therxmv.churros://auth/callback`)
- Future Apple Sign-In steps

---

## Row Level Security (RLS)

RLS is enabled on every table.  The general philosophy:

- **profiles** — owner-only read/write; inserts go through the `handle_new_user` trigger (security definer).
- **households** — any member can read; only parents can write/delete.
- **household_members** — any member can see who else is in their household; only parents can add/change/remove members.
- **chores** — any member can read and create; only the assignee or a parent can update; only parents can delete.
- **notifications** — recipient-only read and mark-as-read; inserts are service-role only (edge functions).

### RLS helper functions

Policies on `households`, `household_members`, and `chores` all need to check whether the current user is a member or parent of a given household.  Doing this with a plain subquery on `household_members` would create a self-referential RLS loop on that table (Postgres evaluates RLS on every row scan, including inner subqueries).

To avoid the loop, three `SECURITY DEFINER` helpers are defined in `04_household_members.sql`.  They run as the function owner, bypassing RLS, and are the **only** place raw `household_members` lookups should appear outside of that file:

| Function | Returns | Used in |
|----------|---------|---------|
| `get_my_household_ids()` | `setof uuid` | `household_members` SELECT policy |
| `is_household_member(uuid)` | `boolean` | `households` + `chores` SELECT/INSERT policies |
| `is_household_parent(uuid)` | `boolean` | all parent-only policies across three tables |

**Rule:** never add a raw `SELECT ... FROM household_members WHERE user_id = auth.uid()` subquery inside a policy.  Call one of these helpers instead.

---

## Realtime

Each table that requires live updates is registered in the `supabase_realtime` publication.  The `ALTER PUBLICATION` statement is co-located at the bottom of each schema file alongside its table definition.

### Enabled tables

| Table | Registered in |
|-------|--------------|
| `profiles` | `02_profiles.sql` |
| `households` | `03_households.sql` |
| `household_members` | `04_household_members.sql` |
| `chores` | `06_chores.sql` |
| `notifications` | `07_notifications.sql` |

### Channel naming convention

Realtime channels in the Android client follow this pattern:

| Table | Channel name format | Example |
|-------|---------------------|---------|
| `profiles` | `profiles:<user_id>` | `profiles:a1b2c3d4-...` |
| `households` | `households:<household_id>` | `households:b2c3d4e5-...` |
| `household_members` | `household_members:<household_id>` | `household_members:b2c3d4e5-...` |
| `chores` | `chores:<household_id>` | `chores:b2c3d4e5-...` |
| `notifications` | `notifications:<user_id>` | `notifications:a1b2c3d4-...` |

Use `household_id` as the filter for household-scoped tables so a single channel covers all members.  Use `user_id` for personal tables (`profiles`, `notifications`).

### RLS and Realtime

Supabase Realtime respects RLS: before broadcasting a change event to a subscriber, it re-evaluates the table's SELECT policy for that subscriber's JWT.  This means:

- A member only receives `chores` or `household_members` broadcasts for their own household.
- A user only receives `notifications` broadcasts addressed to them.
- No extra Realtime-specific policies are needed — the existing SELECT policies are sufficient.

> The `household_members` policies use the `SECURITY DEFINER` helper functions described above, so Realtime RLS evaluation is safe — no self-join cycle at broadcast time.

---

## pg_cron — TTL Jobs

The `notifications` table has a TTL of 1 month.  A `pg_cron` job must be scheduled once per environment via the Supabase SQL editor:

```sql
select cron.schedule(
    'purge-old-notifications',
    '0 3 * * *',
    $$ delete from public.notifications where created_at < now() - interval '1 month' $$
);
```

Run this after the initial schema is applied.  It only needs to be run once; `pg_cron` persists the job in the database.
