package com.therxmv.churros.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.therxmv.churros.feature.home.presentation.PlaceholderHomeScreen

@Composable
fun AppNavGraph() {
    val backStack = rememberNavBackStack(HomeDestination)
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<HomeDestination> { PlaceholderHomeScreen() }
        },
    )
}
