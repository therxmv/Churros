package com.therxmv.churros.feature.settings.di

import com.therxmv.churros.feature.settings.data.repository.SupabaseUserRepository
import com.therxmv.churros.feature.settings.domain.repository.UserRepository
import com.therxmv.churros.feature.settings.domain.usecase.GetProfileUseCase
import com.therxmv.churros.feature.settings.domain.usecase.UpdateAvatarUseCase
import com.therxmv.churros.feature.settings.domain.usecase.UpdateNotificationPreferencesUseCase
import com.therxmv.churros.feature.settings.domain.usecase.UpdateProfileUseCase
import org.koin.dsl.module

val settingsModule = module {
    // Repository — supabaseClient is provided by supabaseModule
    single<UserRepository> {
        SupabaseUserRepository(supabaseClient = get())
    }

    // Use cases
    factory { GetProfileUseCase(repository = get()) }
    factory { UpdateProfileUseCase(repository = get()) }
    factory { UpdateAvatarUseCase(repository = get()) }
    factory { UpdateNotificationPreferencesUseCase(repository = get()) }
}
