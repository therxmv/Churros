package com.therxmv.churros.feature.auth.domain.model

data class LoginCredentials(
    val email: String,
    val password: String,
)

data class RegisterCredentials(
    val name: String,
    val email: String,
    val password: String,
)
