package com.therxmv.churros.feature.chores.domain.model

/**
 * Represents a single household chore.
 *
 * @param dueDate ISO-8601 date string (YYYY-MM-DD). Always required.
 */
data class ChoreItem(
    val id: String,
    val title: String,
    val category: ChoreCategory,
    val dueDate: String,
    val isCompleted: Boolean = false,
)
