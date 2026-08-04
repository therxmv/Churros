package com.therxmv.churros.feature.settings.domain.model

/**
 * Per-type notification toggle settings for a user.
 *
 * Persisted as a JSONB column (`notification_prefs`) in `public.profiles`.
 * All toggles default to enabled, matching the column's Postgres DEFAULT value.
 *
 * Each field corresponds to one value in the `public.notification_type` Postgres enum
 * (`chore_assigned`, `chore_completed`, `chore_edited`, `reward_request`, `daily_goal`).
 */
data class NotificationPreferences(
    /** Notify when a chore is assigned to this user. */
    val choreAssigned: Boolean = true,
    /** Notify when a chore assigned to this user is marked complete. */
    val choreCompleted: Boolean = true,
    /** Notify when a chore assigned to this user is edited. */
    val choreEdited: Boolean = true,
    /** Notify when a reward is requested. */
    val rewardRequest: Boolean = true,
    /** Notify for daily goal reminders. */
    val dailyGoal: Boolean = true,
)
