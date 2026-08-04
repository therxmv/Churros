package com.therxmv.churros.feature.family.di

import com.therxmv.churros.core.database.AppDatabase
import com.therxmv.churros.feature.family.data.repository.SupabaseFamilyRepository
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository
import com.therxmv.churros.feature.family.domain.usecase.GetHouseholdUseCase
import com.therxmv.churros.feature.family.domain.usecase.GetMembersUseCase
import com.therxmv.churros.feature.family.domain.usecase.RemoveMemberUseCase
import com.therxmv.churros.feature.family.domain.usecase.UpdateHouseholdUseCase
import com.therxmv.churros.feature.family.domain.usecase.UploadHouseholdPhotoUseCase
import org.koin.dsl.module

val familyModule = module {
    // DAOs — derived from the AppDatabase singleton provided by platformModule
    single { get<AppDatabase>().householdDao() }
    single { get<AppDatabase>().familyMemberDao() }

    // Repository
    single<FamilyRepository> {
        SupabaseFamilyRepository(
            supabaseClient = get(),
            householdDao = get(),
            familyMemberDao = get(),
        )
    }

    // Use cases
    factory { GetHouseholdUseCase(repository = get()) }
    factory { UpdateHouseholdUseCase(repository = get()) }
    factory { GetMembersUseCase(repository = get()) }
    factory { RemoveMemberUseCase(repository = get()) }
    factory { UploadHouseholdPhotoUseCase(repository = get()) }
}
