package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import com.therxmv.churros.feature.settings.domain.model.UserProfile

class SignInUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<UserProfile> =
        repository.signInWithEmail(email = email, password = password)
}
