package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.repository.AuthRepository

class RequestPasswordResetUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String): Result<Unit> =
        repository.requestPasswordReset(email = email)
}
