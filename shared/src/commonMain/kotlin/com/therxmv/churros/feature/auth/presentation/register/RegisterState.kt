package com.therxmv.churros.feature.auth.presentation.register

import org.jetbrains.compose.resources.StringResource

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: StringResource? = null,
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false,
)
