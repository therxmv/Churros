package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.repository.FamilyRepository
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/** Returns a live [Flow] of all household members as [UserProfile]s, backed by Room + Realtime. */
class GetMembersUseCase(private val repository: FamilyRepository) {
    operator fun invoke(): Flow<List<UserProfile>> = repository.observeMembers()
}
