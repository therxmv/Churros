package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.model.AuthUser
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository

class VerifyEmailOtpUseCase(private val repository: AuthRepository) {

    /**
     * Verifies the 6-digit OTP sent to [email].
     * On success the user is authenticated and the [AuthUser] represents the active session.
     */
    suspend operator fun invoke(email: String, token: String): Result<AuthUser> =
        repository.verifyEmailOtp(email = email, token = token)
}
