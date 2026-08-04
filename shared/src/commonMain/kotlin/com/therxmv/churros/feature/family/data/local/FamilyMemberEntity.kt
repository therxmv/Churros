package com.therxmv.churros.feature.family.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.churros.feature.family.domain.model.toHouseholdRole
import com.therxmv.churros.feature.settings.domain.model.UserProfile
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

/**
 * Maps to [UserProfile].
 *
 * Fields not stored in the local cache ([UserProfile.email], [UserProfile.pushToken],
 * [UserProfile.createdAt], [UserProfile.notificationPreferences]) are set to their defaults.
 */
internal fun FamilyMemberEntity.toDomain(): UserProfile = UserProfile(
    id = userId,
    email = null,
    displayName = displayName,
    avatarUrl = avatarUrl,
    householdId = householdId,
    householdRole = role.toHouseholdRole(),
    joinedAt = Instant.fromEpochMilliseconds(joinedAt),
)
