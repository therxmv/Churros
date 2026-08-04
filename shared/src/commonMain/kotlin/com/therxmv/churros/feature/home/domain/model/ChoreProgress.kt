package com.therxmv.churros.feature.home.domain.model

/**
 * Represents a completed-vs-total chore count for a progress indicator.
 *
 * @property completed Number of chores that have [com.therxmv.churros.feature.chores.domain.model.Chore.completedAt] set today.
 * @property total     Total number of chores scoped to the same set (personal or family-wide).
 */
data class ChoreProgress(
    val completed: Int,
    val total: Int,
) {
    /** Progress fraction in [0f, 1f]; 1f when there are no chores (nothing left to do). */
    val fraction: Float get() = if (total == 0) 1f else completed.toFloat() / total.toFloat()
}
