package com.therxmv.churros.feature.auth.presentation.register

sealed interface RegisterEvent {
    data class NameChanged(val name: String) : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data object SignUpClicked : RegisterEvent
    data object ContinueWithGoogleClicked : RegisterEvent
    data object SignInClicked : RegisterEvent
}
