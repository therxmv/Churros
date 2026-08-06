package com.therxmv.churros.core.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * [SavedStateConfiguration] with every Churros route type registered for polymorphic
 * serialization.
 *
 * Required by [rememberNavBackStack] so the back stack can be saved across process death and
 * configuration changes. All subtypes of [NavKey] used in the app must be listed here.
 */
val churrosNavConfig: SavedStateConfiguration by lazy {
    SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                // ── ScaffoldRoute ──────────────────────────────────────────────
                subclass(ScaffoldRoute.HomeRoute::class)
                subclass(ScaffoldRoute.ChoresRoute::class)
                subclass(ScaffoldRoute.FamilyRoute::class)
                // ── FullscreenRoute ────────────────────────────────────────────
                subclass(FullscreenRoute.ManageFamilyRoute::class)
                subclass(FullscreenRoute.AddMemberRoute::class)
                subclass(FullscreenRoute.PermissionsRoute::class)
                subclass(FullscreenRoute.HouseholdProfileRoute::class)
                subclass(FullscreenRoute.SettingsRoute::class)
                subclass(FullscreenRoute.NotificationsRoute::class)
                subclass(FullscreenRoute.Onboarding1Route::class)
                subclass(FullscreenRoute.SignInRoute::class)
                subclass(FullscreenRoute.SignUpRoute::class)
                subclass(FullscreenRoute.VerifyEmailRoute::class)
                subclass(FullscreenRoute.SetNewPasswordRoute::class)
            }
        }
    }
}
