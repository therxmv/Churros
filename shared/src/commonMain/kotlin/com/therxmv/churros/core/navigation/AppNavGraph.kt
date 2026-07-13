package com.therxmv.churros.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.therxmv.churros.feature.auth.presentation.SplashScreen
import com.therxmv.churros.feature.auth.presentation.login.LoginScreen
import com.therxmv.churros.feature.auth.presentation.register.RegisterScreen
import com.therxmv.churros.feature.home.presentation.HomeScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(SplashDestination::class, SplashDestination.serializer())
            subclass(LoginDestination::class, LoginDestination.serializer())
            subclass(RegisterDestination::class, RegisterDestination.serializer())
            subclass(HomeDestination::class, HomeDestination.serializer())
        }
    }
}

@Composable
fun AppNavGraph() {
    val backStack = rememberNavBackStack(navConfig, SplashDestination)
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<SplashDestination> {
                SplashScreen(
                    onNavigateToLogin = {
                        backStack.removeAll { true }
                        backStack.add(LoginDestination)
                    },
                    onNavigateToRegister = {
                        backStack.removeAll { true }
                        backStack.add(RegisterDestination)
                    },
                )
            }
            entry<LoginDestination> {
                LoginScreen(
                    onNavigateToHome = {
                        backStack.removeAll { true }
                        backStack.add(HomeDestination)
                    },
                    onNavigateToRegister = {
                        backStack.add(RegisterDestination)
                    },
                )
            }
            entry<RegisterDestination> {
                RegisterScreen(
                    onNavigateToHome = {
                        backStack.removeAll { true }
                        backStack.add(HomeDestination)
                    },
                    onNavigateToLogin = {
                        backStack.removeLastOrNull()
                    },
                )
            }
            entry<HomeDestination> { HomeScreen() }
        },
    )
}
