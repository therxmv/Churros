package com.therxmv.churros.feature.auth.presentation.login

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object SignInClicked : LoginEvent
    data object ContinueWithGoogleClicked : LoginEvent
    data object CreateAccountClicked : LoginEvent
}
