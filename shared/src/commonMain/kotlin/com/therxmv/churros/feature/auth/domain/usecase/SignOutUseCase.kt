package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(): Result<Unit> = repository.signOut()
}
