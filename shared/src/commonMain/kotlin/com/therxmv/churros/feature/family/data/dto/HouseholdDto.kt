package com.therxmv.churros.feature.family.data.dto

import com.therxmv.churros.feature.family.data.local.HouseholdEntity
import com.therxmv.churros.feature.family.domain.model.Household
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Supabase PostgREST / Realtime wire DTO for the `public.households` table.
 *
 * All timestamp fields arrive as ISO-8601 strings from PostgREST.
 */
@Serializable
data class HouseholdDto(
    val id: String,
    val name: String,
    val address: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("created_at") val createdAt: String,
)

// ---------------------------------------------------------------------------
// Mapping helpers
// ---------------------------------------------------------------------------

internal fun HouseholdDto.toDomain(): Household = Household(
    id = id,
    name = name,
    address = address,
    photoUrl = photoUrl,
    createdAt = Instant.parse(createdAt),
)

internal fun HouseholdDto.toEntity(): HouseholdEntity = HouseholdEntity(
    id = id,
    name = name,
    address = address,
    photoUrl = photoUrl,
    createdAt = Instant.parse(createdAt).toEpochMilliseconds(),
)
