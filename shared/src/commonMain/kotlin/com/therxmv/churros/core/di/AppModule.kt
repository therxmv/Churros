package com.therxmv.churros.core.di

import com.therxmv.churros.feature.auth.di.authModule
import com.therxmv.churros.feature.chores.di.choresModule
import com.therxmv.churros.feature.family.di.familyModule
import org.koin.dsl.module

val appModule = module {
    includes(platformModule, supabaseModule, networkModule, authModule, choresModule, familyModule)
}
