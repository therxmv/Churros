package com.therxmv.churros.feature.home.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model for a single entry in the Home activity feed.
 *
 * Backed by a row in `public.notifications`. New items stream in via Supabase Realtime
 * without a full-list refresh — see [com.therxmv.churros.feature.home.domain.usecase.ObserveActivityFeedUseCase].
 *
 * @property id          Supabase UUID of the notification row.
 * @property type        Category that drives how the UI renders the item.
 * @property payload     Context-specific JSONB payload from Supabase (e.g. chore title,
 *                       requester name). Keys and values are string-typed for forward
 *                       compatibility.
 * @property isRead      Whether the recipient has already seen this item.
 * @property createdAt   UTC timestamp of when the notification was created.
 */
data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val payload: Map<String, String>,
    val isRead: Boolean,
    val createdAt: Instant,
)
