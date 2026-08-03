package com.therxmv.churros.di

import com.therxmv.churros.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val credentialsModule = module {
    single(named("supabaseUrl")) { BuildConfig.SUPABASE_URL }
    single(named("supabaseKey")) { BuildConfig.SUPABASE_ANON_KEY }
}
