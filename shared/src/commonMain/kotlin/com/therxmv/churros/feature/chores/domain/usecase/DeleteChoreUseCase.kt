package com.therxmv.churros.feature.chores.domain.usecase

import com.therxmv.churros.feature.chores.domain.repository.ChoreRepository

class DeleteChoreUseCase(private val repository: ChoreRepository) {

    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteChore(id)
}
