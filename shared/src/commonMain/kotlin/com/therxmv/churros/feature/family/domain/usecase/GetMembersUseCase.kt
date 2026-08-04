package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.model.MemberProfile
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow

class GetMembersUseCase(private val repository: FamilyRepository) {
    operator fun invoke(): Flow<List<MemberProfile>> = repository.observeMembers()
}
