package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.model.AuthUser
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(): AuthUser? = repository.getCurrentUser()
}
