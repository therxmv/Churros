package com.therxmv.churros.core.di

import com.therxmv.churros.feature.auth.di.authModule
import com.therxmv.churros.feature.home.di.homeModule
import org.koin.dsl.module

val appModule = module {
    includes(authModule, homeModule)
}
