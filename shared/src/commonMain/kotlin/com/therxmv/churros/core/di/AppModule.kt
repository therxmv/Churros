package com.therxmv.churros.core.di

import org.koin.dsl.module

val appModule = module {
    includes(platformModule)
}
