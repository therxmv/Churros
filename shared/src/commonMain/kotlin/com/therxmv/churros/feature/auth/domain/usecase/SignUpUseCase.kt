package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import com.therxmv.churros.feature.settings.domain.model.UserProfile

class SignUpUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<UserProfile> =
        repository.signUpWithEmail(email = email, password = password)
}
