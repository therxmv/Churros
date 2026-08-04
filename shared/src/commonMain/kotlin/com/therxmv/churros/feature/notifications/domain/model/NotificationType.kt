package com.therxmv.churros.feature.notifications.domain.model

/**
 * Mirrors the `public.notification_type` Postgres enum.
 *
 * Determines how a [Notification] is rendered in the notifications feed:
 * - [REWARD_REQUEST] is actionable (Approve / Decline) — Phase 3 UI.
 * - All others are informational.
 */
enum class NotificationType {
    CHORE_ASSIGNED,
    CHORE_COMPLETED,
    CHORE_EDITED,
    REWARD_REQUEST,
    DAILY_GOAL,
    UNKNOWN,
}

fun String.toNotificationType(): NotificationType = when (this) {
    "chore_assigned" -> NotificationType.CHORE_ASSIGNED
    "chore_completed" -> NotificationType.CHORE_COMPLETED
    "chore_edited" -> NotificationType.CHORE_EDITED
    "reward_request" -> NotificationType.REWARD_REQUEST
    "daily_goal" -> NotificationType.DAILY_GOAL
    else -> NotificationType.UNKNOWN
}
