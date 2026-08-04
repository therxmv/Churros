package com.therxmv.churros.feature.settings.domain.model

import com.therxmv.churros.feature.family.domain.model.HouseholdRole
import kotlinx.datetime.Instant

/**
 * Unified domain model representing the current authenticated user's complete profile.
 *
 * Consolidates data from three sources:
 * - `auth.users`              — [id], [email]
 * - `public.profiles`         — [displayName], [avatarUrl], [pushToken], [createdAt],
 *                               [notificationPreferences]
 * - `public.household_members`— [householdId], [householdRole], [joinedAt]
 *
 * Household-specific fields ([householdId], [householdRole], [joinedAt]) are null when
 * the user has not yet joined or created a household.
 *
 * [createdAt] and [pushToken] are null when the profile is built solely from auth metadata
 * (e.g. inside [com.therxmv.churros.feature.auth.data.repository.SupabaseAuthRepository]
 * without a `public.profiles` fetch).
 *
 * This model replaces the previously separate `AuthUser` (auth feature) and `FamilyMember`
 * (family feature) to avoid redundant profile representations.
 */
data class UserProfile(
    /** Supabase auth user UUID. Also the primary key in `public.profiles`. */
    val id: String,

    /** Email address from `auth.users`. Null for social-only sign-ins (Google, Apple). */
    val email: String?,

    /** Display name from `public.profiles.display_name`. */
    val displayName: String,

    /** Avatar URL from `public.profiles.avatar_url`. Null until an avatar is uploaded. */
    val avatarUrl: String?,

    /**
     * FCM device push token from `public.profiles.push_token`. Updated on login.
     * Null when the profile is built from auth metadata only.
     */
    val pushToken: String? = null,

    /**
     * When the `public.profiles` row was created.
     * Null when the profile is built from auth metadata only.
     */
    val createdAt: Instant? = null,

    /** UUID of the household this user belongs to, or null if not in any household. */
    val householdId: String? = null,

    /** Role within the household, or null if not in any household. */
    val householdRole: HouseholdRole? = null,

    /** When the user joined their household, or null if not in any household. */
    val joinedAt: Instant? = null,

    /** Per-notification-type toggle preferences. All enabled by default. */
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
)
