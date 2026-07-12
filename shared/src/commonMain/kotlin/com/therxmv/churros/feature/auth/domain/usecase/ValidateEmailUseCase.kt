package com.therxmv.churros.feature.auth.domain.usecase

class ValidateEmailUseCase {
    private val emailRegex = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")

    operator fun invoke(email: String): Boolean = emailRegex.matches(email.trim())
}
