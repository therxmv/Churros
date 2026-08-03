package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.model.AuthUser
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<AuthUser> =
        repository.signUpWithEmail(email = email, password = password)
}
