package com.therxmv.churros.feature.family.data.dto

import com.therxmv.churros.feature.family.data.local.FamilyMemberEntity
import com.therxmv.churros.feature.family.domain.model.toHouseholdRole
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Supabase PostgREST / Realtime wire DTO for the `public.household_members` table,
 * with the `profiles` row embedded via a PostgREST select join
 * (`select=*,profiles(display_name,avatar_url)`).
 */
@Serializable
data class FamilyMemberDto(
    @SerialName("user_id") val userId: String,
    @SerialName("household_id") val householdId: String,
    /** One of "parent", "kid" — matches the `household_role` Postgres enum. */
    val role: String,
    @SerialName("joined_at") val joinedAt: String,
    /** Embedded join from `public.profiles`. Null-safe: absent when Realtime sends a bare record. */
    val profiles: ProfileDto? = null,
)

/**
 * Minimal DTO for the `public.profiles` row embedded in [FamilyMemberDto].
 */
@Serializable
data class ProfileDto(
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

// ---------------------------------------------------------------------------
// Mapping helpers
// ---------------------------------------------------------------------------

/**
 * Maps to [UserProfile].
 *
 * Falls back to [userId] as the display name when the profiles join is absent
 * (e.g., when the profile row was deleted or the join is not included in the query).
 *
 * Fields not available from this DTO ([UserProfile.email], [UserProfile.pushToken],
 * [UserProfile.createdAt], [UserProfile.notificationPreferences]) are set to their
 * defaults.
 */
internal fun FamilyMemberDto.toDomain(): UserProfile = UserProfile(
    id = userId,
    email = null,
    displayName = profiles?.displayName ?: userId,
    avatarUrl = profiles?.avatarUrl,
    householdId = householdId,
    householdRole = role.toHouseholdRole(),
    joinedAt = Instant.parse(joinedAt),
)

internal fun FamilyMemberDto.toEntity(): FamilyMemberEntity = FamilyMemberEntity(
    userId = userId,
    householdId = householdId,
    role = role,
    displayName = profiles?.displayName ?: userId,
    avatarUrl = profiles?.avatarUrl,
    joinedAt = Instant.parse(joinedAt).toEpochMilliseconds(),
)
