package com.therxmv.churros.feature.auth.presentation.login

import org.jetbrains.compose.resources.StringResource

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false,
)
