package com.therxmv.churros.feature.family.domain.model

/** Role a member holds within a household. Mirrors the `household_role` Postgres enum. */
enum class HouseholdRole { PARENT, KID }

fun String.toHouseholdRole(): HouseholdRole = when (lowercase()) {
    "parent" -> HouseholdRole.PARENT
    else -> HouseholdRole.KID
}

fun HouseholdRole.toDto(): String = when (this) {
    HouseholdRole.PARENT -> "parent"
    HouseholdRole.KID -> "kid"
}
