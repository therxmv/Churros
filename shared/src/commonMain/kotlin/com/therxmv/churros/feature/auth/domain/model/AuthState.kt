package com.therxmv.churros.feature.auth.domain.model

sealed class AuthState {

    data object Loading : AuthState()

    data class Authenticated(val user: AuthUser) : AuthState()

    data object Unauthenticated : AuthState()
}
