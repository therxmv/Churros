package com.therxmv.churros.feature.family.domain.model

import kotlinx.datetime.Instant

data class MemberProfile(
    val id: String,
    val displayName: String,
    val avatarUrl: String?,
    val householdId: String,
    val householdRole: HouseholdRole,
    val joinedAt: Instant,
)
