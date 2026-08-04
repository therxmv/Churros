package com.therxmv.churros.feature.home.di

import com.therxmv.churros.feature.home.data.repository.SupabaseHomeRepository
import com.therxmv.churros.feature.home.domain.repository.HomeRepository
import com.therxmv.churros.feature.home.domain.usecase.GetDashboardDataUseCase
import com.therxmv.churros.feature.home.domain.usecase.ObserveActivityFeedUseCase
import org.koin.dsl.module

val homeModule = module {
    // Repository — supabaseClient is provided by supabaseModule
    single<HomeRepository> {
        SupabaseHomeRepository(supabaseClient = get())
    }

    // Use cases
    factory { GetDashboardDataUseCase(repository = get()) }
    factory { ObserveActivityFeedUseCase(repository = get()) }
}
