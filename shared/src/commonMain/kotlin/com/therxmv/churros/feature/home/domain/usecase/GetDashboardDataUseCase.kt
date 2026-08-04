package com.therxmv.churros.feature.home.domain.usecase

import com.therxmv.churros.feature.home.domain.model.DashboardData
import com.therxmv.churros.feature.home.domain.repository.HomeRepository

/**
 * Fetches a fresh [DashboardData] snapshot for the Home screen.
 *
 * This is a one-shot suspend call (not a Flow) because the dashboard loads its
 * aggregate data on entry and refreshes on pull-to-refresh. Live updates to the
 * activity feed are handled separately by [ObserveActivityFeedUseCase].
 */
class GetDashboardDataUseCase(private val repository: HomeRepository) {

    suspend operator fun invoke(): Result<DashboardData> = repository.getDashboardData()
}
