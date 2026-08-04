package com.therxmv.churros.feature.family.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model representing a single member of a household.
 *
 * Profile fields ([displayName], [avatarUrl]) are denormalised from the
 * `profiles` table and embedded here for convenient use by the presentation layer.
 *
 * @property userId      Supabase auth user UUID — also the `profiles.id` FK.
 * @property householdId UUID of the household this member belongs to.
 * @property role        Parent or Kid role within the household.
 * @property displayName User's display name from `profiles.display_name`.
 * @property avatarUrl   User's avatar URL from `profiles.avatar_url`, or null.
 * @property joinedAt    When the user joined the household.
 */
data class FamilyMember(
    val userId: String,
    val householdId: String,
    val role: HouseholdRole,
    val displayName: String,
    val avatarUrl: String?,
    val joinedAt: Instant,
)
