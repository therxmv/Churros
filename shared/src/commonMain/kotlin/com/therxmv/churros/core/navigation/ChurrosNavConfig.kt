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
                subclass(HomeRoute::class)
                subclass(ChoresRoute::class)
                subclass(FamilyRoute::class)
                subclass(ManageFamilyRoute::class)
                subclass(AddMemberRoute::class)
                subclass(PermissionsRoute::class)
                subclass(HouseholdProfileRoute::class)
                subclass(SettingsRoute::class)
                subclass(NotificationsRoute::class)
                // ── FullscreenRoute ────────────────────────────────────────────
                subclass(Onboarding1Route::class)
                subclass(Onboarding2Route::class)
                subclass(Onboarding3Route::class)
                subclass(SignInRoute::class)
                subclass(SignUpRoute::class)
                subclass(VerifyEmailRoute::class)
                subclass(SetNewPasswordRoute::class)
            }
        }
    }
}
