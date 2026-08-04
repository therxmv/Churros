package com.therxmv.churros.feature.chores.domain.model

/** Priority level of a chore, mirroring the `chore_priority` Postgres enum. */
enum class ChorePriority { LOW, MEDIUM, HIGH }

fun String.toChorePriority(): ChorePriority = when (lowercase()) {
    "low" -> ChorePriority.LOW
    "high" -> ChorePriority.HIGH
    else -> ChorePriority.MEDIUM
}

fun ChorePriority.toDto(): String = when (this) {
    ChorePriority.LOW -> "low"
    ChorePriority.MEDIUM -> "medium"
    ChorePriority.HIGH -> "high"
}
