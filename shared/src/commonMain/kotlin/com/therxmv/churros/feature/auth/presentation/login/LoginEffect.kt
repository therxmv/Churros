package com.therxmv.churros.feature.auth.presentation.login

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToRegister : LoginEffect
}
