package com.therxmv.churros.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * CompositionLocal providing access to the app-level [NavBackStack] from any composable in the
 * hierarchy.
 *
 * Feature screens use this to push, pop, or replace destinations:
 * ```kotlin
 * val backStack = LocalNavBackStack.current
 * backStack.add(VerifyEmailRoute(email = email))
 *
 * // Navigate to Home after auth, clearing the auth back stack:
 * backStack.clear()
 * backStack.add(HomeRoute)
 * ```
 */
val LocalNavBackStack = compositionLocalOf<NavBackStack<NavKey>> {
    error("LocalNavBackStack not provided — ensure App() wraps the content")
}
