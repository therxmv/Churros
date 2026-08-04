package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import com.therxmv.churros.feature.settings.domain.model.UserProfile

class VerifyEmailOtpUseCase(private val repository: AuthRepository) {

    /**
     * Verifies the 6-digit OTP sent to [email].
     * On success the user is authenticated and the [UserProfile] represents the active session.
     */
    suspend operator fun invoke(email: String, token: String): Result<UserProfile> =
        repository.verifyEmailOtp(email = email, token = token)
}
