package com.therxmv.churros.feature.settings.domain.repository

import com.therxmv.churros.feature.settings.domain.model.NotificationPreferences
import com.therxmv.churros.feature.settings.domain.model.UserProfile

interface UserRepository {

    /**
     * Fetches the current authenticated user's full profile from Supabase.
     *
     * Combines data from three tables:
     * - `auth.users` for [UserProfile.email]
     * - `public.profiles` for display name, avatar, push token, notification prefs
     * - `public.household_members` for household affiliation and role (may be absent)
     *
     * One-shot fetch — no local cache or Realtime subscription.
     */
    suspend fun getProfile(): Result<UserProfile>

    /**
     * Updates [UserProfile.displayName] in `public.profiles` and returns the
     * refreshed [UserProfile].
     */
    suspend fun updateProfile(displayName: String): Result<UserProfile>

    /**
     * Uploads [imageBytes] to the `avatars` Supabase Storage bucket at the path
     * `{user_id}/avatar.jpg` (upsert — any previous avatar is replaced), then stores the
     * resulting URL in `public.profiles.avatar_url`.
     *
     * Returns the updated [UserProfile] (with the new [UserProfile.avatarUrl]) on success.
     */
    suspend fun uploadAvatar(imageBytes: ByteArray): Result<UserProfile>

    /**
     * Persists [preferences] to `public.profiles.notification_prefs` (JSONB column added
     * in migration `10_notification_prefs.sql`) and returns the refreshed [UserProfile].
     */
    suspend fun updateNotificationPreferences(preferences: NotificationPreferences): Result<UserProfile>
}
