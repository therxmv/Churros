package com.therxmv.churros.feature.chores.presentation

import com.therxmv.churros.feature.chores.domain.model.ChoreCategory
import com.therxmv.churros.feature.chores.domain.model.ChoreFilter

sealed interface ChoresEvent {
    data object AddChoreClicked : ChoresEvent
    data object AddChoreSheetDismissed : ChoresEvent
    data class AddTitleChanged(val title: String) : ChoresEvent
    data class AddCategorySelected(val category: ChoreCategory) : ChoresEvent
    data class AddDueDateChanged(val date: String) : ChoresEvent
    data object SaveChoreClicked : ChoresEvent
    data class ChoreCompletionToggled(val choreId: String, val isCompleted: Boolean) : ChoresEvent
    data class ChoreDeleted(val choreId: String) : ChoresEvent
    data class FilterSelected(val filter: ChoreFilter) : ChoresEvent
}
