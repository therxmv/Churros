package com.therxmv.churros.feature.chores.presentation

import androidx.lifecycle.ViewModel
import com.therxmv.churros.feature.chores.domain.model.ChoreCategory
import com.therxmv.churros.feature.chores.domain.model.ChoreFilter
import com.therxmv.churros.feature.chores.domain.model.ChoreItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class ChoresViewModel : ViewModel() {

    private val mockChores = listOf(
        ChoreItem(id = "1", title = "Vacuum living room", category = ChoreCategory.CLEANING, dueDate = "2026-07-12"),
        ChoreItem(id = "2", title = "Cook dinner", category = ChoreCategory.COOKING, dueDate = "2026-07-12"),
        ChoreItem(id = "3", title = "Buy groceries", category = ChoreCategory.SHOPPING, dueDate = "2026-07-13"),
        ChoreItem(id = "4", title = "Wash laundry", category = ChoreCategory.LAUNDRY, dueDate = "2026-07-14", isCompleted = true),
        ChoreItem(id = "5", title = "Fix kitchen light", category = ChoreCategory.OTHER, dueDate = "2026-07-18"),
        ChoreItem(id = "6", title = "Fix kitchen light", category = ChoreCategory.OTHER, dueDate = "2026-07-18"),
    )

    private val _state = MutableStateFlow(
        ChoresState(chores = mockChores).withComputedDisplayData(),
    )
    val state: StateFlow<ChoresState> = _state.asStateFlow()

    private val _effects = Channel<ChoresEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: ChoresEvent) {
        when (event) {
            ChoresEvent.AddChoreClicked -> _state.update { it.copy(isAddSheetVisible = true) }
            ChoresEvent.AddChoreSheetDismissed -> resetAddSheet()
            is ChoresEvent.AddTitleChanged -> _state.update {
                it.copy(addChoreTitle = event.title, addChoreTitleError = null)
            }
            is ChoresEvent.AddCategorySelected -> _state.update { it.copy(addChoreCategory = event.category) }
            is ChoresEvent.AddDueDateChanged -> _state.update { it.copy(addChoreDueDate = event.date) }
            ChoresEvent.SaveChoreClicked -> saveChore()
            is ChoresEvent.ChoreCompletionToggled -> toggleCompletion(event.choreId, event.isCompleted)
            is ChoresEvent.ChoreDeleted -> deleteChore(event.choreId)
            is ChoresEvent.FilterSelected -> _state.update {
                it.copy(activeFilter = event.filter).withComputedDisplayData()
            }
        }
    }

    private fun saveChore() {
        val current = _state.value
        val title = current.addChoreTitle.trim()
        if (title.isBlank()) {
            _state.update { it.copy(addChoreTitleError = "TITLE_REQUIRED") }
            return
        }
        val dueDate = current.addChoreDueDate.ifBlank { todayIsoString() }
        val newChore = ChoreItem(
            id = kotlin.random.Random.nextLong().toString(),
            title = title,
            category = current.addChoreCategory,
            dueDate = dueDate,
        )
        _state.update { it.copy(chores = it.chores + newChore).withComputedDisplayData() }
        resetAddSheet()
    }

    private fun toggleCompletion(choreId: String, isCompleted: Boolean) {
        _state.update { current ->
            current.copy(
                chores = current.chores.map { chore ->
                    if (chore.id == choreId) chore.copy(isCompleted = isCompleted) else chore
                },
            ).withComputedDisplayData()
        }
    }

    private fun deleteChore(choreId: String) {
        _state.update { current ->
            current.copy(chores = current.chores.filter { it.id != choreId }).withComputedDisplayData()
        }
    }

    private fun resetAddSheet() {
        _state.update {
            it.copy(
                isAddSheetVisible = false,
                addChoreTitle = "",
                addChoreTitleError = null,
                addChoreCategory = ChoreCategory.CLEANING,
                addChoreDueDate = "",
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/**
 * Recomputes all derived display state:
 * - [ChoresState.progress] — completion ratio within the active filter window.
 * - [ChoresState.filteredAndGroupedChores] — filtered, grouped, and sorted sections
 *   ready for the screen to render without any further transformation.
 */
private fun ChoresState.withComputedDisplayData(): ChoresState {
    val today = todayIsoString()
    val weekEnd = weekEndIsoString()

    val filtered = chores.filteredBy(activeFilter, today, weekEnd)

    val total = filtered.size
    val completed = filtered.count { it.isCompleted }
    val progress = if (total > 0) completed.toFloat() / total else 0f

    val sections = filtered
        .groupBy { chore ->
            when {
                chore.dueDate == today -> ChoreSectionHeader.Today
                chore.dueDate in today..weekEnd -> ChoreSectionHeader.ThisWeek
                else -> ChoreSectionHeader.Later(chore.dueDate)
            }
        }
        .entries
        .sortedBy { (header, _) -> header }
        .map { (header, chores) ->
            ChoreSection(
                header = header,
                chores = chores.sortedWith(
                    compareBy<ChoreItem> { it.isCompleted }.thenBy { it.dueDate },
                ),
            )
        }

    return copy(progress = progress, filteredAndGroupedChores = sections)
}

/**
 * Filters chores by the given [ChoreFilter].
 * Date comparison is lexicographic — safe for YYYY-MM-DD ISO strings.
 * [today] and [weekEnd] are passed in to avoid recomputing them per item.
 */
private fun List<ChoreItem>.filteredBy(filter: ChoreFilter, today: String, weekEnd: String): List<ChoreItem> =
    when (filter) {
        ChoreFilter.TODAY -> filter { it.dueDate == today }
        ChoreFilter.THIS_WEEK -> filter { it.dueDate >= today && it.dueDate <= weekEnd }
        ChoreFilter.ALL -> this
    }

/**
 * Returns today's date as YYYY-MM-DD.
 * Uses a simple platform-agnostic implementation via kotlinx epoch helpers.
 * Falls back to a hardcoded date for compile-safety in common code.
 *
 * NOTE: Replace with kotlinx-datetime once the dependency is added.
 */
internal expect fun todayIsoString(): String

/**
 * Returns the date 6 days from today (end of the current week window) as YYYY-MM-DD.
 */
internal expect fun weekEndIsoString(): String
