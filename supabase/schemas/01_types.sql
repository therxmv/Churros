-- =============================================================================
-- Types / Enums
-- =============================================================================

-- Roles a user can hold within a household.
create type if not exists public.household_role as enum (
    'parent',
    'kid',
    'caregiver'
);
