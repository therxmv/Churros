package com.therxmv.churros.feature.auth.domain.usecase

class ValidateNameUseCase {
    operator fun invoke(name: String): Boolean = name.trim().isNotEmpty()
}
