package com.therxmv.churros.feature.chores.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Minimal DTO for the `public.household_members` table.
 * Used only to resolve the current user's `household_id`.
 */
@Serializable
internal data class HouseholdMemberDto(
    @SerialName("household_id") val householdId: String,
)
