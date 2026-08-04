package com.therxmv.churros.core.di

import com.therxmv.churros.core.locale.localeModule
import com.therxmv.churros.feature.auth.di.authModule
import com.therxmv.churros.feature.chores.di.choresModule
import com.therxmv.churros.feature.family.di.familyModule
import com.therxmv.churros.feature.home.di.homeModule
import com.therxmv.churros.feature.notifications.di.notificationsModule
import com.therxmv.churros.feature.settings.di.settingsModule
import org.koin.dsl.module

val appModule = module {
    includes(
        platformModule,
        supabaseModule,
        networkModule,
        localeModule,
        authModule,
        choresModule,
        familyModule,
        settingsModule,
        homeModule,
        notificationsModule,
    )
}
