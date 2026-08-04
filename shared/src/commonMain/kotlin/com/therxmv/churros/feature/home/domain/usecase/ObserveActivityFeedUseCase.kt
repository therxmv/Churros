package com.therxmv.churros.feature.home.domain.usecase

import com.therxmv.churros.feature.home.domain.model.ActivityItem
import com.therxmv.churros.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Exposes a live [Flow] of [ActivityItem]s backed by a Supabase Realtime subscription.
 *
 * The list starts with recent notifications fetched from `public.notifications` and
 * grows as new items arrive via the Realtime channel — no full-page refresh needed.
 *
 * Collect this flow while the Home screen is visible; cancelling the collector
 * automatically unsubscribes the Realtime channel.
 */
class ObserveActivityFeedUseCase(private val repository: HomeRepository) {

    operator fun invoke(): Flow<List<ActivityItem>> = repository.observeActivityFeed()
}
