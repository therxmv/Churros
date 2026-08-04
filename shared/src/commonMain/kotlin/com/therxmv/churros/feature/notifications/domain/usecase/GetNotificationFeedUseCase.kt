package com.therxmv.churros.feature.notifications.domain.usecase

import com.therxmv.churros.feature.notifications.domain.model.NotificationFeed
import com.therxmv.churros.feature.notifications.domain.repository.NotificationsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Exposes a live [Flow] of [NotificationFeed] backed by a Supabase Realtime subscription.
 *
 * The feed is pre-grouped into Recent (last 24 h) and Earlier buckets. New notifications
 * appear at the top of the Recent bucket in real time; mark-as-read updates are reflected
 * immediately without requiring a manual refresh.
 *
 * Collect while the Notifications screen is visible; cancelling the collector automatically
 * unsubscribes the Realtime channel.
 */
class GetNotificationFeedUseCase(private val repository: NotificationsRepository) {

    operator fun invoke(): Flow<NotificationFeed> = repository.getNotificationFeed()
}
