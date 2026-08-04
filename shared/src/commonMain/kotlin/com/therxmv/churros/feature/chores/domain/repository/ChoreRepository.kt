package com.therxmv.churros.feature.chores.domain.repository

import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.model.ChorePriority
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ChoreRepository {

    /**
     * Returns a cold [Flow] that emits the full list of chores for the authenticated
     * user's household. Room is the source of truth; Realtime events from Supabase
     * are written to Room in the background while this flow is collected, so the
     * list updates live across all connected devices.
     *
     * The initial emission comes from the Room cache (possibly empty on first launch).
     * A full Supabase fetch runs concurrently to populate Room on first collection.
     *
     * The flow's coroutine scope manages the Realtime channel lifecycle: the channel
     * is subscribed when collection starts and removed when the collector cancels.
     */
    fun observeChores(): Flow<List<Chore>>

    /**
     * Creates a new chore in the current household and caches it in Room.
     */
    suspend fun createChore(
        title: String,
        category: String?,
        assigneeId: String?,
        dueAt: Instant?,
        repeatSchedule: String?,
        priority: ChorePriority,
        rewardPoints: Int,
    ): Result<Chore>

    /**
     * Updates the mutable fields of an existing chore and refreshes the Room cache.
     */
    suspend fun updateChore(
        id: String,
        title: String,
        category: String?,
        assigneeId: String?,
        dueAt: Instant?,
        repeatSchedule: String?,
        priority: ChorePriority,
        rewardPoints: Int,
    ): Result<Chore>

    /**
     * Marks a chore as completed by setting `completed_at` to the current UTC time.
     * Returns the updated [Chore] on success.
     */
    suspend fun completeChore(id: String): Result<Chore>

    /**
     * Permanently deletes a chore. Only household parents are allowed by RLS.
     */
    suspend fun deleteChore(id: String): Result<Unit>
}
