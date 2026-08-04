package com.therxmv.churros.feature.notifications.domain.model

/**
 * The grouped notification feed shown on the Notifications screen.
 *
 * Partitioned client-side by [GetNotificationFeedUseCase]:
 * - [recent]  — notifications created within the last 24 hours.
 * - [earlier] — all older notifications.
 *
 * Both lists are ordered newest-first (matching the Supabase query order).
 */
data class NotificationFeed(
    val recent: List<Notification>,
    val earlier: List<Notification>,
)
