package com.therxmv.churros.feature.chores.di

import com.therxmv.churros.core.database.AppDatabase
import com.therxmv.churros.feature.chores.data.repository.SupabaseChoreRepository
import com.therxmv.churros.feature.chores.domain.repository.ChoreRepository
import com.therxmv.churros.feature.chores.domain.usecase.CompleteChoreUseCase
import com.therxmv.churros.feature.chores.domain.usecase.CreateChoreUseCase
import com.therxmv.churros.feature.chores.domain.usecase.DeleteChoreUseCase
import com.therxmv.churros.feature.chores.domain.usecase.GetChoresUseCase
import com.therxmv.churros.feature.chores.domain.usecase.UpdateChoreUseCase
import org.koin.dsl.module

val choresModule = module {
    // DAO — derived from the AppDatabase singleton provided by platformModule
    single { get<AppDatabase>().choreDao() }

    // Repository
    single<ChoreRepository> {
        SupabaseChoreRepository(
            supabaseClient = get(),
            choreDao = get(),
        )
    }

    // Use cases
    factory { GetChoresUseCase(repository = get()) }
    factory { CreateChoreUseCase(repository = get()) }
    factory { UpdateChoreUseCase(repository = get()) }
    factory { CompleteChoreUseCase(repository = get()) }
    factory { DeleteChoreUseCase(repository = get()) }
}
