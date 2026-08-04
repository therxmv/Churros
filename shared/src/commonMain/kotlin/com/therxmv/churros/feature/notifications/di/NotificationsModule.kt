package com.therxmv.churros.feature.notifications.di

import com.therxmv.churros.feature.notifications.data.repository.SupabaseNotificationsRepository
import com.therxmv.churros.feature.notifications.domain.repository.NotificationsRepository
import com.therxmv.churros.feature.notifications.domain.usecase.GetNotificationFeedUseCase
import com.therxmv.churros.feature.notifications.domain.usecase.MarkNotificationReadUseCase
import org.koin.dsl.module

val notificationsModule = module {
    // Repository — supabaseClient is provided by supabaseModule
    single<NotificationsRepository> {
        SupabaseNotificationsRepository(supabaseClient = get())
    }

    // Use cases
    factory { GetNotificationFeedUseCase(repository = get()) }
    factory { MarkNotificationReadUseCase(repository = get()) }
}
