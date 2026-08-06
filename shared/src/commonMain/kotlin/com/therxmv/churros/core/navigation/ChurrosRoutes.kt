package com.therxmv.churros.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface ScaffoldRoute : NavKey {
    @Serializable data object HomeRoute : ScaffoldRoute
    @Serializable data object ChoresRoute : ScaffoldRoute
    @Serializable data object FamilyRoute : ScaffoldRoute
}

sealed interface FullscreenRoute : NavKey {
    // Family sub-screens
    @Serializable data object ManageFamilyRoute : FullscreenRoute
    @Serializable data object AddMemberRoute : FullscreenRoute
    @Serializable data object PermissionsRoute : FullscreenRoute
    @Serializable data object HouseholdProfileRoute : FullscreenRoute

    // Top-bar push destinations
    @Serializable data object SettingsRoute : FullscreenRoute
    @Serializable data object NotificationsRoute : FullscreenRoute

    // Onboarding slides (shown once on first launch)
    @Serializable data object Onboarding1Route : FullscreenRoute
    @Serializable data object Onboarding2Route : FullscreenRoute
    @Serializable data object Onboarding3Route : FullscreenRoute

    // Auth flow
    @Serializable data object SignInRoute : FullscreenRoute
    @Serializable data object SignUpRoute : FullscreenRoute
    @Serializable data class VerifyEmailRoute(val email: String) : FullscreenRoute
    @Serializable data object SetNewPasswordRoute : FullscreenRoute
}
