package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.repository.FamilyRepository

/** Removes the member with [userId] from the household. Only parents are permitted by RLS. */
class RemoveMemberUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(userId: String): Result<Unit> = repository.removeMember(userId)
}
