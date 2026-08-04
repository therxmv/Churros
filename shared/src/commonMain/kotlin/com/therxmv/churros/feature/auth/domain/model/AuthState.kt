package com.therxmv.churros.feature.auth.domain.model

import com.therxmv.churros.feature.settings.domain.model.UserProfile

sealed class AuthState {

    data object Loading : AuthState()

    data class Authenticated(val user: UserProfile) : AuthState()

    data object Unauthenticated : AuthState()
}
