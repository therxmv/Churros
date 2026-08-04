package com.therxmv.churros.feature.notifications.domain.usecase

import com.therxmv.churros.feature.notifications.domain.repository.NotificationsRepository

/**
 * Marks a single notification as read by setting `is_read = true` in `public.notifications`.
 *
 * The Realtime UPDATE event triggered by this write will automatically update the
 * feed emitted by [GetNotificationFeedUseCase] — no separate refresh is needed.
 *
 * @param notificationId Supabase UUID of the target notification row.
 */
class MarkNotificationReadUseCase(private val repository: NotificationsRepository) {

    suspend operator fun invoke(notificationId: String): Result<Unit> =
        repository.markAsRead(notificationId)
}
