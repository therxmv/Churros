package com.therxmv.churros.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.therxmv.churros.feature.home.presentation.PlaceholderHomeScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(HomeDestination::class, HomeDestination.serializer())
        }
    }
}

@Composable
fun AppNavGraph() {
    val backStack = rememberNavBackStack(navConfig, HomeDestination)
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<HomeDestination> { PlaceholderHomeScreen() }
        },
    )
}
