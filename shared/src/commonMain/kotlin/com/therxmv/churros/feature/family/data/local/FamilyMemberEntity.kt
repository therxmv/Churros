package com.therxmv.churros.feature.family.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.churros.feature.family.domain.model.MemberProfile
import com.therxmv.churros.feature.family.domain.model.toHouseholdRole
import kotlinx.datetime.Instant

/**
 * Room entity for the local household-members cache.
 *
 * Profile fields ([displayName], [avatarUrl]) are denormalised from the `profiles` table
 * and cached here so the UI can display member names and avatars without a network call.
 *
 * The `joinedAt` timestamp is stored as epoch milliseconds.
 */
@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey val userId: String,
    val householdId: String,
    /** One of "parent", "kid". */
    val role: String,
    val displayName: String,
    val avatarUrl: String?,
    /** Epoch millis. */
    val joinedAt: Long,
)

// ---------------------------------------------------------------------------
// Mapping helper
// ---------------------------------------------------------------------------

internal fun FamilyMemberEntity.toDomain(): MemberProfile = MemberProfile(
    id = userId,
    displayName = displayName,
    avatarUrl = avatarUrl,
    householdId = householdId,
    householdRole = role.toHouseholdRole(),
    joinedAt = Instant.fromEpochMilliseconds(joinedAt),
)
