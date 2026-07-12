package com.therxmv.churros.feature.auth.domain.usecase

class ValidatePasswordUseCase {
    private val minLength = 8

    operator fun invoke(password: String): Boolean = password.length >= minLength
}
