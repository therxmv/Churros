package com.therxmv.churros.feature.home.presentation

sealed interface HomeEvent {
    data class TaskCheckedChanged(val taskId: String, val isChecked: Boolean) : HomeEvent
}
