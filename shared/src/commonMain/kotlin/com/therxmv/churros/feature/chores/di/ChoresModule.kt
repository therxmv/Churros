package com.therxmv.churros.feature.chores.di

import com.therxmv.churros.feature.chores.presentation.ChoresViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val choresModule = module {
    viewModel { ChoresViewModel() }
}
