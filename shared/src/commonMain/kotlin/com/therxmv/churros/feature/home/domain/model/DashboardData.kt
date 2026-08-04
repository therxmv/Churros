package com.therxmv.churros.feature.home.domain.model

import com.therxmv.churros.feature.chores.domain.model.Chore

/**
 * Composite domain model for the Home dashboard.
 *
 * All data is derived from a single snapshot fetched at load time.
 * Live updates to the activity feed use a separate
 * [com.therxmv.churros.feature.home.domain.usecase.ObserveActivityFeedUseCase].
 *
 * @property familyName       Name of the household (used in the personalised greeting).
 * @property currentUserName  Display name of the authenticated user.
 * @property personalProgress Completed / total chores assigned to the current user today.
 * @property familyProgress   Completed / total chores across all household members today.
 * @property upcomingChores   Today's open chores sorted by [Chore.dueAt] (nulls last).
 */
data class DashboardData(
    val familyName: String,
    val currentUserName: String,
    val personalProgress: ChoreProgress,
    val familyProgress: ChoreProgress,
    val upcomingChores: List<Chore>,
)
