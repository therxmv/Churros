package com.therxmv.churros.feature.notifications.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model for a single entry in the household notifications feed.
 *
 * Backed by a row in `public.notifications`. Items are grouped into
 * [NotificationFeed.recent] (last 24 h) and [NotificationFeed.earlier] buckets
 * by [GetNotificationFeedUseCase].
 *
 * @property id        Supabase UUID of the notification row.
 * @property type      Category that drives how the UI renders the item.
 *                     [NotificationType.REWARD_REQUEST] is actionable (Phase 3 UI).
 * @property payload   Context-specific JSONB from Supabase. Keys vary by [type]
 *                     (e.g. "chore_title", "requester_name"). Values are plain strings.
 * @property isRead    Whether the recipient has acknowledged this notification.
 * @property createdAt UTC timestamp of insertion.
 */
data class Notification(
    val id: String,
    val type: NotificationType,
    val payload: Map<String, String>,
    val isRead: Boolean,
    val createdAt: Instant,
)
