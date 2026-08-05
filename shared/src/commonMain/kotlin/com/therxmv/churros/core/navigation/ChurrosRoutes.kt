package com.therxmv.churros.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// ── Base route interfaces ─────────────────────────────────────────────────────

/**
 * Destination rendered inside [ChurrosScaffoldScreen] — shows the bottom navigation bar.
 *
 * Covers: main tabs (Home / Chores / Family), Family sub-screens, and top-bar destinations
 * (Settings, Notifications) that keep the scaffold chrome visible.
 */
sealed interface ScaffoldRoute : NavKey

/**
 * Destination rendered inside [ChurrosFullScreen] — no bottom navigation bar.
 *
 * Covers: Onboarding slides and the full Auth flow.
 */
sealed interface FullscreenRoute : NavKey

// ── ScaffoldRoute destinations ────────────────────────────────────────────────

// Bottom-nav tabs
@Serializable data object HomeRoute : ScaffoldRoute
@Serializable data object ChoresRoute : ScaffoldRoute
@Serializable data object FamilyRoute : ScaffoldRoute

// Family sub-screens (push onto global back stack; Family tab stays highlighted)
@Serializable data object ManageFamilyRoute : ScaffoldRoute
@Serializable data object AddMemberRoute : ScaffoldRoute
@Serializable data object PermissionsRoute : ScaffoldRoute
@Serializable data object HouseholdProfileRoute : ScaffoldRoute

// Top-bar push destinations
@Serializable data object SettingsRoute : ScaffoldRoute
@Serializable data object NotificationsRoute : ScaffoldRoute

// ── FullscreenRoute destinations ──────────────────────────────────────────────

// Onboarding slides (shown once on first launch)
@Serializable data object Onboarding1Route : FullscreenRoute
@Serializable data object Onboarding2Route : FullscreenRoute
@Serializable data object Onboarding3Route : FullscreenRoute

// Auth flow
@Serializable data object SignInRoute : FullscreenRoute
@Serializable data object SignUpRoute : FullscreenRoute
@Serializable data class VerifyEmailRoute(val email: String) : FullscreenRoute
@Serializable data object SetNewPasswordRoute : FullscreenRoute
