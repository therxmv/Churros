package com.therxmv.churros.feature.chores.presentation

import com.therxmv.churros.feature.chores.domain.model.ChoreItem

/**
 * Represents a single date-grouped section of chores in the list.
 * Produced by the ViewModel so the screen is render-only.
 */
data class ChoreSection(
    val header: ChoreSectionHeader,
    val chores: List<ChoreItem>,
)

/**
 * The logical date grouping used as a section header.
 */
sealed class ChoreSectionHeader : Comparable<ChoreSectionHeader> {
    data object Today : ChoreSectionHeader()
    data object ThisWeek : ChoreSectionHeader()
    data class Later(val date: String) : ChoreSectionHeader()

    override fun compareTo(other: ChoreSectionHeader): Int = sortKey.compareTo(other.sortKey)

    private val sortKey: String
        get() = when (this) {
            Today -> "0_today"
            ThisWeek -> "1_this_week"
            is Later -> "2_$date"
        }
}
