package com.therxmv.churros.feature.onboarding.di

import com.therxmv.churros.feature.onboarding.data.repository.DataStoreOnboardingRepository
import com.therxmv.churros.feature.onboarding.domain.repository.OnboardingRepository
import com.therxmv.churros.feature.onboarding.domain.usecase.IsOnboardingSeenUseCase
import com.therxmv.churros.feature.onboarding.domain.usecase.MarkOnboardingSeenUseCase
import org.koin.dsl.module

val onboardingModule = module {
    single<OnboardingRepository> {
        DataStoreOnboardingRepository(dataStore = get())
    }

    factory { IsOnboardingSeenUseCase(repository = get()) }
    factory { MarkOnboardingSeenUseCase(repository = get()) }
}
