-- =============================================================================
-- notification_prefs
-- Adds a per-type notification toggle column to public.profiles.
--
-- Each key in the JSONB object corresponds to one value of the
-- public.notification_type enum:
--   chore_assigned  — user is assigned a chore
--   chore_completed — a chore assigned to the user is marked complete
--   chore_edited    — a chore assigned to the user is edited
--   reward_request  — a reward is requested
--   daily_goal      — daily goal reminder
--
-- The DEFAULT sets every toggle to true, matching the app's
-- NotificationPreferences data class defaults.
-- =============================================================================

alter table public.profiles
    add column if not exists notification_prefs jsonb not null
        default '{"chore_assigned":true,"chore_completed":true,"chore_edited":true,"reward_request":true,"daily_goal":true}'::jsonb;
