package com.therxmv.churros.feature.settings.domain.usecase

import com.therxmv.churros.feature.settings.domain.model.UserProfile
import com.therxmv.churros.feature.settings.domain.repository.UserRepository

/**
 * Updates the current user's display name in `public.profiles` and returns the
 * refreshed [UserProfile].
 */
class UpdateProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(displayName: String): Result<UserProfile> =
        repository.updateProfile(displayName = displayName)
}
