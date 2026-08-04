package com.therxmv.churros.core.locale

import org.koin.dsl.module

val localeModule = module {
    single { LocaleManager() }
}
