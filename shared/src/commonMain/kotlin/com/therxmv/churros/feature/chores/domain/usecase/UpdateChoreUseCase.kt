package com.therxmv.churros.feature.chores.domain.usecase

import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.model.ChorePriority
import com.therxmv.churros.feature.chores.domain.repository.ChoreRepository
import kotlinx.datetime.Instant

class UpdateChoreUseCase(private val repository: ChoreRepository) {

    suspend operator fun invoke(
        id: String,
        title: String,
        category: String? = null,
        assigneeId: String? = null,
        dueAt: Instant? = null,
        repeatSchedule: String? = null,
        priority: ChorePriority = ChorePriority.MEDIUM,
        rewardPoints: Int = 0,
    ): Result<Chore> = repository.updateChore(
        id = id,
        title = title,
        category = category,
        assigneeId = assigneeId,
        dueAt = dueAt,
        repeatSchedule = repeatSchedule,
        priority = priority,
        rewardPoints = rewardPoints,
    )
}
