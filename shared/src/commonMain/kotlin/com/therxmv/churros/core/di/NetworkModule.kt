package com.therxmv.churros.core.di

import com.therxmv.churros.core.network.createHttpClient
import io.github.jan.supabase.SupabaseClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        createHttpClient(
            engine = get<HttpClientEngine>(),
            supabaseClient = get<SupabaseClient>(),
        )
    }
}
