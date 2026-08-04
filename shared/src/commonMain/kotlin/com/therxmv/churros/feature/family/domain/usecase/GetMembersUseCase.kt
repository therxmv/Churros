package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.model.FamilyMember
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow

/** Returns a live [Flow] of all [FamilyMember]s in the household, backed by Room + Realtime. */
class GetMembersUseCase(private val repository: FamilyRepository) {
    operator fun invoke(): Flow<List<FamilyMember>> = repository.observeMembers()
}
