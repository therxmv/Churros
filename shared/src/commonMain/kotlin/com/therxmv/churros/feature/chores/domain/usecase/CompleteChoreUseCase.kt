package com.therxmv.churros.feature.chores.domain.usecase

import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.repository.ChoreRepository

class CompleteChoreUseCase(private val repository: ChoreRepository) {

    suspend operator fun invoke(id: String): Result<Chore> = repository.completeChore(id)
}
