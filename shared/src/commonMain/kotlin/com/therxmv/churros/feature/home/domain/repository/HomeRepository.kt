package com.therxmv.churros.feature.home.domain.repository

import com.therxmv.churros.feature.home.domain.model.ActivityItem
import com.therxmv.churros.feature.home.domain.model.DashboardData
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    /**
     * Fetches a fresh [DashboardData] snapshot for the Home screen.
     *
     * Queries chores and household/member data directly from Supabase in a single
     * async call. Chore counts (personal + family) are derived client-side from the
     * today's chores list.
     *
     * Returns [com.therxmv.churros.feature.home.domain.model.HomeError.Unauthorized] when
     * the session has expired, or [com.therxmv.churros.feature.home.domain.model.HomeError.HouseholdNotFound]
     * when the user has not joined a household.
     */
    suspend fun getDashboardData(): Result<DashboardData>

    /**
     * Returns a cold [Flow] that emits the current user's notification/activity feed
     * and keeps it live via a Supabase Realtime subscription on `public.notifications`.
     *
     * The initial emission is the full list of recent notifications fetched from Supabase.
     * Each Realtime INSERT event prepends the new item to the list without requiring a
     * full refresh.
     *
     * The Realtime channel lifecycle is tied to the flow's coroutine scope: it is
     * subscribed when collection starts and removed when the collector cancels.
     */
    fun observeActivityFeed(): Flow<List<ActivityItem>>
}
