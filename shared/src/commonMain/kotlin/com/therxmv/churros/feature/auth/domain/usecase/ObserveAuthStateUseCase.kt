package com.therxmv.churros.feature.auth.domain.usecase

import com.therxmv.churros.feature.auth.domain.model.AuthState
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(private val repository: AuthRepository) {

    operator fun invoke(): Flow<AuthState> = repository.observeAuthState()
}
