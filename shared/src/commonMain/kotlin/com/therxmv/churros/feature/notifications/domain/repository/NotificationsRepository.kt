package com.therxmv.churros.feature.notifications.domain.repository

import com.therxmv.churros.feature.notifications.domain.model.NotificationFeed
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {

    /**
     * Returns a cold [Flow] that emits the current user's notification feed grouped into
     * [NotificationFeed.recent] (last 24 h) and [NotificationFeed.earlier] buckets.
     *
     * The initial emission is fetched from Supabase. Subsequent emissions are triggered
     * by Supabase Realtime events (INSERT for new notifications, UPDATE when [markAsRead]
     * flips `is_read`). The Realtime channel lifecycle is tied to the flow's coroutine
     * scope — it is subscribed when collection starts and removed when the collector cancels.
     */
    fun getNotificationFeed(): Flow<NotificationFeed>

    /**
     * Marks the notification with [notificationId] as read by setting `is_read = true`
     * in `public.notifications`.
     *
     * RLS enforces that only the recipient can update their own notifications.
     * The Realtime UPDATE event emitted by Supabase will automatically refresh the
     * feed [Flow] returned by [getNotificationFeed].
     */
    suspend fun markAsRead(notificationId: String): Result<Unit>
}
