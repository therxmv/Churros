package com.therxmv.churros.core.di

import com.therxmv.churros.feature.auth.di.authModule
import org.koin.dsl.module

val appModule = module {
    includes(authModule)
}
