package com.therxmv.churros.feature.auth.di

import com.therxmv.churros.feature.auth.data.repository.SupabaseAuthRepository
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import com.therxmv.churros.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.therxmv.churros.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.therxmv.churros.feature.auth.domain.usecase.RequestPasswordResetUseCase
import com.therxmv.churros.feature.auth.domain.usecase.SetNewPasswordUseCase
import com.therxmv.churros.feature.auth.domain.usecase.SignInUseCase
import com.therxmv.churros.feature.auth.domain.usecase.SignOutUseCase
import com.therxmv.churros.feature.auth.domain.usecase.SignUpUseCase
import com.therxmv.churros.feature.auth.domain.usecase.VerifyEmailOtpUseCase
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> {
        SupabaseAuthRepository(supabaseClient = get())
    }

    factory { SignInUseCase(repository = get()) }
    factory { SignUpUseCase(repository = get()) }
    factory { SignOutUseCase(repository = get()) }
    factory { GetCurrentUserUseCase(repository = get()) }
    factory { ObserveAuthStateUseCase(repository = get()) }
    factory { RequestPasswordResetUseCase(repository = get()) }
    factory { VerifyEmailOtpUseCase(repository = get()) }
    factory { SetNewPasswordUseCase(repository = get()) }
}
