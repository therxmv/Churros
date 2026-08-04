package com.therxmv.churros.feature.settings.data.repository

import co.touchlab.kermit.Logger
import com.therxmv.churros.feature.settings.data.dto.HouseholdMembershipDto
import com.therxmv.churros.feature.settings.data.dto.NotificationPrefsDto
import com.therxmv.churros.feature.settings.data.dto.UserProfileDto
import com.therxmv.churros.feature.settings.data.dto.toDomain
import com.therxmv.churros.feature.settings.data.dto.toDto
import com.therxmv.churros.feature.settings.domain.model.NotificationPreferences
import com.therxmv.churros.feature.settings.domain.model.SettingsError
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import com.therxmv.churros.feature.settings.domain.repository.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SupabaseUserRepository(
    private val supabaseClient: SupabaseClient,
) : UserRepository {

    private val logger = Logger.withTag("SupabaseUserRepository")

    // ---------------------------------------------------------------------------
    // UserRepository
    // ---------------------------------------------------------------------------

    override suspend fun getProfile(): Result<UserProfile> = runCatching {
        val userId = requireUserId()
        fetchProfile(userId)
    }.mapSettingsError()

    override suspend fun updateProfile(displayName: String): Result<UserProfile> = runCatching {
        val userId = requireUserId()
        val dto = supabaseClient.postgrest.from("profiles")
            .update(UpdateDisplayNameDto(displayName = displayName)) {
                filter { eq("id", userId) }
                select()
            }
            .decodeSingle<UserProfileDto>()
        val membership = fetchMembership(userId)
        val email = supabaseClient.auth.currentUserOrNull()?.email
        dto.toDomain(membership = membership, email = email)
    }.mapSettingsError()

    override suspend fun uploadAvatar(imageBytes: ByteArray): Result<UserProfile> = runCatching {
        val userId = requireUserId()

        // Path convention: {user_id}/avatar.jpg
        // The RLS policy in 08_storage.sql checks (storage.foldername(name))[1] = auth.uid()::text,
        // which requires the first path segment to match the user's ID.
        val path = "$userId/avatar.jpg"
        supabaseClient.storage.from("avatars").upload(path, imageBytes) {
            upsert = true
        }
        val avatarUrl = supabaseClient.storage.from("avatars").publicUrl(path)

        val dto = supabaseClient.postgrest.from("profiles")
            .update(UpdateAvatarDto(avatarUrl = avatarUrl)) {
                filter { eq("id", userId) }
                select()
            }
            .decodeSingle<UserProfileDto>()
        val membership = fetchMembership(userId)
        val email = supabaseClient.auth.currentUserOrNull()?.email
        dto.toDomain(membership = membership, email = email)
    }.mapSettingsError()

    override suspend fun updateNotificationPreferences(
        preferences: NotificationPreferences,
    ): Result<UserProfile> = runCatching {
        val userId = requireUserId()
        val dto = supabaseClient.postgrest.from("profiles")
            .update(UpdateNotificationPrefsDto(notificationPrefs = preferences.toDto())) {
                filter { eq("id", userId) }
                select()
            }
            .decodeSingle<UserProfileDto>()
        val membership = fetchMembership(userId)
        val email = supabaseClient.auth.currentUserOrNull()?.email
        dto.toDomain(membership = membership, email = email)
    }.mapSettingsError()

    // ---------------------------------------------------------------------------
    // Private helpers
    // ---------------------------------------------------------------------------

    private fun requireUserId(): String =
        supabaseClient.auth.currentUserOrNull()?.id ?: throw SettingsError.Unauthorized

    /**
     * Fetches the full profile for [userId] by combining:
     * - A `public.profiles` row (display name, avatar, push token, notification prefs)
     * - An optional `public.household_members` row (household affiliation and role)
     * - The email from the live auth session (`auth.users`)
     */
    private suspend fun fetchProfile(userId: String): UserProfile {
        val dto = supabaseClient.postgrest.from("profiles")
            .select { filter { eq("id", userId) } }
            .decodeSingle<UserProfileDto>()
        val membership = fetchMembership(userId)
        val email = supabaseClient.auth.currentUserOrNull()?.email
        return dto.toDomain(membership = membership, email = email)
    }

    /**
     * Returns the current user's household membership row, or null if they have not
     * joined or created a household yet.
     *
     * Failures are logged and swallowed so that a missing or inaccessible
     * `household_members` row does not prevent the profile from loading.
     */
    private suspend fun fetchMembership(userId: String): HouseholdMembershipDto? =
        runCatching {
            supabaseClient.postgrest.from("household_members")
                .select {
                    filter { eq("user_id", userId) }
                    limit(1)
                }
                .decodeSingleOrNull<HouseholdMembershipDto>()
        }.onFailure { logger.e("fetchMembership failed for $userId: $it") }.getOrNull()

    private fun Throwable.toSettingsError(): SettingsError = when {
        this is SettingsError -> this
        this is HttpRequestException -> SettingsError.NetworkError
        this is RestException && statusCode == 401 -> SettingsError.Unauthorized
        this is RestException && statusCode == 403 -> SettingsError.Unauthorized
        this is RestException && statusCode == 404 -> SettingsError.ProfileNotFound
        else -> SettingsError.Unknown(message = message, cause = this)
    }

    private fun <T> Result<T>.mapSettingsError(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toSettingsError()) },
    )

    // ---------------------------------------------------------------------------
    // Internal write DTOs (not exposed outside this file)
    // ---------------------------------------------------------------------------

    @Serializable
    private data class UpdateDisplayNameDto(
        @SerialName("display_name") val displayName: String,
    )

    @Serializable
    private data class UpdateAvatarDto(
        @SerialName("avatar_url") val avatarUrl: String,
    )

    @Serializable
    private data class UpdateNotificationPrefsDto(
        @SerialName("notification_prefs") val notificationPrefs: NotificationPrefsDto,
    )
}
