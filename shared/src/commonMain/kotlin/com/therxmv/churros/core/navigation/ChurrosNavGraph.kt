package com.therxmv.churros.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.therxmv.churros.core.design.components.ChurrosFullScreen
import com.therxmv.churros.core.design.components.ChurrosScaffoldScreen

/**
 * Root navigation graph for the Churros app.
 *
 * Sets up a single global [NavBackStack] starting at [startDestination], wraps it in
 * [LocalNavBackStack] so any composable in the tree can push/pop destinations, and delegates
 * rendering to [NavDisplay].
 *
 * Route type determines the surrounding chrome:
 * - [ScaffoldRoute] → [ChurrosScaffoldScreen] (bottom navigation bar visible)
 * - [FullscreenRoute] → [ChurrosFullScreen] (no navigation chrome)
 *
 * **Clearing auth/onboarding history:**
 * Feature ViewModels navigate to [ScaffoldRoute.HomeRoute] after successful sign-in by calling:
 * ```kotlin
 * val backStack = LocalNavBackStack.current
 * backStack.clear()
 * backStack.add(ScaffoldRoute.HomeRoute)
 * ```
 * This ensures back from Home exits the app rather than returning to the auth flow.
 *
 * @param startDestination The first destination placed on the back stack. Determined at startup
 *   by [com.therxmv.churros.AppViewModel] based on the onboarding-seen flag and auth state.
 */
@Composable
fun ChurrosNavGraph(startDestination: NavKey) {
    val backStack = rememberNavBackStack(churrosNavConfig, startDestination)

    CompositionLocalProvider(LocalNavBackStack provides backStack) {
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                // ── ScaffoldRoute destinations ─────────────────────────────────

                entry<ScaffoldRoute.HomeRoute> { route ->
                    ChurrosScaffoldScreen(
                        currentRoute = route,
                        onTabSelected = { backStack.navigateToTab(it) },
                    ) {
                        Box(Modifier.fillMaxSize()) // TODO: HomeScreen
                    }
                }

                entry<ScaffoldRoute.ChoresRoute> { route ->
                    ChurrosScaffoldScreen(
                        currentRoute = route,
                        onTabSelected = { backStack.navigateToTab(it) },
                    ) {
                        Box(Modifier.fillMaxSize()) // TODO: ChoresScreen
                    }
                }

                entry<ScaffoldRoute.FamilyRoute> { route ->
                    ChurrosScaffoldScreen(
                        currentRoute = route,
                        onTabSelected = { backStack.navigateToTab(it) },
                    ) {
                        Box(Modifier.fillMaxSize()) // TODO: FamilyDashboardScreen
                    }
                }

                // ── FullscreenRoute destinations ───────────────────────────────

                entry<FullscreenRoute.ManageFamilyRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: ManageFamilyScreen
                    }
                }

                entry<FullscreenRoute.AddMemberRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: AddMemberScreen
                    }
                }

                entry<FullscreenRoute.PermissionsRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: PermissionsScreen
                    }
                }

                entry<FullscreenRoute.HouseholdProfileRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: HouseholdProfileScreen
                    }
                }

                entry<FullscreenRoute.SettingsRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: SettingsScreen
                    }
                }

                entry<FullscreenRoute.NotificationsRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: NotificationsScreen
                    }
                }

                entry<FullscreenRoute.Onboarding1Route> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: OnboardingScreen (slide 1)
                    }
                }

                entry<FullscreenRoute.Onboarding2Route> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: OnboardingScreen (slide 2)
                    }
                }

                entry<FullscreenRoute.Onboarding3Route> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: OnboardingScreen (slide 3)
                    }
                }

                entry<FullscreenRoute.SignInRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: SignInScreen
                    }
                }

                entry<FullscreenRoute.SignUpRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: SignUpScreen
                    }
                }

                entry<FullscreenRoute.VerifyEmailRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: VerifyEmailScreen
                    }
                }

                entry<FullscreenRoute.SetNewPasswordRoute> {
                    ChurrosFullScreen {
                        Box(Modifier.fillMaxSize()) // TODO: SetNewPasswordScreen
                    }
                }
            },
        )
    }
}

// ── Back-stack helpers ────────────────────────────────────────────────────────

/**
 * Navigates to a bottom-nav tab by root route.
 *
 * If the target is already the last entry, this is a no-op (prevents duplicate entries).
 * Otherwise the root route is pushed onto the stack so back navigation returns to the
 * previous destination.
 *
 * Tabs never pop the entire stack — only the auth/onboarding → Home transition clears it.
 */
private fun NavBackStack<NavKey>.navigateToTab(route: ScaffoldRoute) {
    if (lastOrNull() == route) return
    add(route)
}
