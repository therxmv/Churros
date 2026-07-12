package com.therxmv.churros.feature.auth.presentation.register

sealed interface RegisterEffect {
    data object NavigateToHome : RegisterEffect
    data object NavigateToLogin : RegisterEffect
}
