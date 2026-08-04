package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import com.therxmv.churros.feature.settings.domain.model.UserProfile

class GetCurrentUserUseCase(private val repository: AuthRepository) {

    /**
     * Returns the currently signed-in user built from auth metadata only, or null.
     *
     * Profile-specific fields ([UserProfile.pushToken], [UserProfile.notificationPreferences],
     * household fields) are not populated here. Use [GetProfileUseCase] for the full profile.
     */
    suspend operator fun invoke(): UserProfile? = repository.getCurrentUser()
}
