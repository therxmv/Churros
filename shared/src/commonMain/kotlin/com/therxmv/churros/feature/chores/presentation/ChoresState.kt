package com.therxmv.churros.feature.chores.presentation

import com.therxmv.churros.feature.chores.domain.model.ChoreCategory
import com.therxmv.churros.feature.chores.domain.model.ChoreFilter
import com.therxmv.churros.feature.chores.domain.model.ChoreItem

data class ChoresState(
    // Source of truth — used by the ViewModel for mutations; not read directly by the screen.
    val chores: List<ChoreItem> = emptyList(),
    val activeFilter: ChoreFilter = ChoreFilter.ALL,
    // Pre-computed display data — the screen renders these without any further transformation.
    val progress: Float = 0f,
    val filteredAndGroupedChores: List<ChoreSection> = emptyList(),
    // Add-chore sheet fields
    val isAddSheetVisible: Boolean = false,
    val addChoreTitle: String = "",
    val addChoreTitleError: String? = null,
    val addChoreCategory: ChoreCategory = ChoreCategory.CLEANING,
    val addChoreDueDate: String = "",
)
