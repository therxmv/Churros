package com.therxmv.churros.feature.family.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model representing a household (the shared-home unit).
 *
 * @property id        Supabase UUID primary key.
 * @property name      Human-readable household name.
 * @property address   Optional street/city address.
 * @property photoUrl  URL of the household cover photo stored in the
 *                     `family-photos` Supabase Storage bucket, or null.
 * @property createdAt When the household was created.
 */
data class Household(
    val id: String,
    val name: String,
    val address: String?,
    val photoUrl: String?,
    val createdAt: Instant,
)
