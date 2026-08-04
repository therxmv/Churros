package com.therxmv.churros.feature.settings.data.dto

import com.therxmv.churros.feature.family.domain.model.toHouseholdRole
import com.therxmv.churros.feature.settings.domain.model.NotificationPreferences
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Supabase PostgREST wire DTO for a row in `public.profiles`.
 *
 * The `notification_prefs` JSONB column is added by migration `10_notification_prefs.sql`.
 * All fields default to the Postgres column defaults so older rows (created before the
 * migration ran) deserialize cleanly.
 */
@Serializable
data class UserProfileDto(
    val id: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("push_token") val pushToken: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("notification_prefs") val notificationPrefs: NotificationPrefsDto = NotificationPrefsDto(),
)

/**
 * JSONB shape of `public.profiles.notification_prefs`.
 *
 * Every field defaults to `true`, matching the Postgres column DEFAULT.
 * Using `ignoreUnknownKeys = true` (set on the repository's Json instance) future-proofs
 * deserialization when new notification types are added to the Postgres enum.
 */
@Serializable
data class NotificationPrefsDto(
    @SerialName("chore_assigned") val choreAssigned: Boolean = true,
    @SerialName("chore_completed") val choreCompleted: Boolean = true,
    @SerialName("chore_edited") val choreEdited: Boolean = true,
    @SerialName("reward_request") val rewardRequest: Boolean = true,
    @SerialName("daily_goal") val dailyGoal: Boolean = true,
)

/**
 * Minimal DTO for the optional `public.household_members` row fetched alongside the profile.
 */
@Serializable
data class HouseholdMembershipDto(
    @SerialName("household_id") val householdId: String,
    val role: String,
    @SerialName("joined_at") val joinedAt: String,
)

// ---------------------------------------------------------------------------
// Mapping helpers
// ---------------------------------------------------------------------------

internal fun NotificationPrefsDto.toDomain() = NotificationPreferences(
    choreAssigned = choreAssigned,
    choreCompleted = choreCompleted,
    choreEdited = choreEdited,
    rewardRequest = rewardRequest,
    dailyGoal = dailyGoal,
)

internal fun NotificationPreferences.toDto() = NotificationPrefsDto(
    choreAssigned = choreAssigned,
    choreCompleted = choreCompleted,
    choreEdited = choreEdited,
    rewardRequest = rewardRequest,
    dailyGoal = dailyGoal,
)

/**
 * Maps a [UserProfileDto] to a [UserProfile] domain model.
 *
 * @param membership optional household membership row — null when the user has not joined
 *   any household yet.
 * @param email      email address from `auth.users`; not stored in `public.profiles`.
 */
internal fun UserProfileDto.toDomain(
    membership: HouseholdMembershipDto? = null,
    email: String? = null,
): UserProfile = UserProfile(
    id = id,
    email = email,
    displayName = displayName,
    avatarUrl = avatarUrl,
    pushToken = pushToken,
    createdAt = Instant.parse(createdAt),
    householdId = membership?.householdId,
    householdRole = membership?.role?.toHouseholdRole(),
    joinedAt = membership?.joinedAt?.let { Instant.parse(it) },
    notificationPreferences = notificationPrefs.toDomain(),
)
