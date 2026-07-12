package com.therxmv.churros.feature.home.domain.model

data class TaskItem(
    val id: String,
    val title: String,
    val isChecked: Boolean = false,
)
