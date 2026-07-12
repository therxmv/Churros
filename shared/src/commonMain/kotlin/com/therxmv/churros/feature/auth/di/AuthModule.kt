package com.therxmv.churros.feature.auth.di

import com.therxmv.churros.feature.auth.domain.usecase.ValidateEmailUseCase
import com.therxmv.churros.feature.auth.domain.usecase.ValidateNameUseCase
import com.therxmv.churros.feature.auth.domain.usecase.ValidatePasswordUseCase
import com.therxmv.churros.feature.auth.presentation.login.LoginViewModel
import com.therxmv.churros.feature.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    factory { ValidateEmailUseCase() }
    factory { ValidatePasswordUseCase() }
    factory { ValidateNameUseCase() }

    viewModel {
        LoginViewModel(
            validateEmail = get(),
            validatePassword = get(),
        )
    }

    viewModel {
        RegisterViewModel(
            validateName = get(),
            validateEmail = get(),
            validatePassword = get(),
        )
    }
}
