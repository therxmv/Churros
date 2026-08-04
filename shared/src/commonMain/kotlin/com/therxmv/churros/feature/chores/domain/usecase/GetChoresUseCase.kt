package com.therxmv.churros.feature.chores.domain.usecase

import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.model.ChoreFilter
import com.therxmv.churros.feature.chores.domain.model.DateBucket
import com.therxmv.churros.feature.chores.domain.repository.ChoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * Returns a live [Flow] of chores, optionally filtered by assignee and/or date bucket.
 *
 * Filtering is performed client-side on each emission so it works seamlessly with
 * the Realtime-backed Room cache (no extra network calls needed per filter change).
 */
class GetChoresUseCase(private val repository: ChoreRepository) {

    operator fun invoke(filter: ChoreFilter = ChoreFilter()): Flow<List<Chore>> =
        repository.observeChores().map { chores ->
            chores.filter { chore ->
                matchesAssignee(chore, filter.assigneeId) &&
                    matchesDateBucket(chore, filter.dateBucket)
            }
        }

    private fun matchesAssignee(chore: Chore, assigneeId: String?): Boolean =
        assigneeId == null || chore.assigneeId == assigneeId

    @OptIn(ExperimentalTime::class)
    private fun matchesDateBucket(chore: Chore, bucket: DateBucket?): Boolean {
        if (bucket == null) return true

        val tz = TimeZone.currentSystemDefault()
        val today = Clock.System.now().toLocalDateTime(tz).date

        return when (bucket) {
            DateBucket.DONE -> chore.completedAt != null

            DateBucket.TODAY -> {
                if (chore.completedAt != null) return false
                val dueDate = chore.dueAt?.toLocalDateTime(tz)?.date ?: return false
                dueDate == today
            }

            DateBucket.TOMORROW -> {
                if (chore.completedAt != null) return false
                val dueDate = chore.dueAt?.toLocalDateTime(tz)?.date ?: return false
                dueDate == today.plus(1, DateTimeUnit.DAY)
            }
        }
    }
}
