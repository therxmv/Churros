package com.therxmv.churros.feature.home.presentation

import com.therxmv.churros.feature.home.domain.model.TaskItem

data class HomeState(
    val userName: String = "",
    val tasks: List<TaskItem> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
)
