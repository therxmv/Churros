package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.model.Household
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow

/** Returns a live [Flow] of the authenticated user's [Household], backed by Room + Realtime. */
class GetHouseholdUseCase(private val repository: FamilyRepository) {
    operator fun invoke(): Flow<Household?> = repository.observeHousehold()
}
