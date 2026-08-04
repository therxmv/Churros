package com.therxmv.churros.feature.family.domain.usecase

import com.therxmv.churros.feature.family.domain.model.Household
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository

/** Updates the household's [name] and optional [address]. Only parents are permitted by RLS. */
class UpdateHouseholdUseCase(private val repository: FamilyRepository) {
    suspend operator fun invoke(
        name: String,
        address: String? = null,
    ): Result<Household> = repository.updateHousehold(name = name, address = address)
}
