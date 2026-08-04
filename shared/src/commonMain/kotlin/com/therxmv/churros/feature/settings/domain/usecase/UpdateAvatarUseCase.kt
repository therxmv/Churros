package com.therxmv.churros.feature.settings.domain.usecase

import com.therxmv.churros.feature.settings.domain.model.UserProfile
import com.therxmv.churros.feature.settings.domain.repository.UserRepository

/**
 * Uploads [imageBytes] to the `avatars` Supabase Storage bucket at the path
 * `{user_id}/avatar.jpg` (upsert — any previous avatar is replaced), then stores the
 * resulting public URL in `public.profiles.avatar_url`.
 *
 * Access is governed by the storage RLS policies defined in `08_storage.sql`:
 * only the authenticated owner may write their own avatar object.
 *
 * Returns the updated [UserProfile] (including the new [UserProfile.avatarUrl]) on success.
 */
class UpdateAvatarUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<UserProfile> =
        repository.uploadAvatar(imageBytes = imageBytes)
}
