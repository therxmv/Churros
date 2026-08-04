package com.therxmv.churros.feature.home.domain.model

/**
 * Mirrors the `public.notification_type` Postgres enum.
 *
 * Used to determine how an [ActivityItem] is rendered in the activity feed.
 */
enum class ActivityType {
    CHORE_ASSIGNED,
    CHORE_COMPLETED,
    CHORE_EDITED,
    REWARD_REQUEST,
    DAILY_GOAL,
    UNKNOWN,
}

fun String.toActivityType(): ActivityType = when (this) {
    "chore_assigned" -> ActivityType.CHORE_ASSIGNED
    "chore_completed" -> ActivityType.CHORE_COMPLETED
    "chore_edited" -> ActivityType.CHORE_EDITED
    "reward_request" -> ActivityType.REWARD_REQUEST
    "daily_goal" -> ActivityType.DAILY_GOAL
    else -> ActivityType.UNKNOWN
}
