package com.therxmv.churros.feature.home.presentation

import androidx.lifecycle.ViewModel
import com.therxmv.churros.feature.home.domain.model.TaskItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val mockTasks = listOf(
        TaskItem(id = "1", title = "Take out trash"),
        TaskItem(id = "2", title = "Vacuum living room"),
        TaskItem(id = "3", title = "Wash dishes"),
        TaskItem(id = "4", title = "Feed the cat"),
    )

    private val _state = MutableStateFlow(
        HomeState(
            userName = "Roman",
            tasks = mockTasks,
            completedCount = mockTasks.count { it.isChecked },
            totalCount = mockTasks.size,
        )
    )
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.TaskCheckedChanged -> toggleTask(event.taskId, event.isChecked)
        }
    }

    private fun toggleTask(taskId: String, isChecked: Boolean) {
        _state.update { current ->
            val updatedTasks = current.tasks.map { task ->
                if (task.id == taskId) task.copy(isChecked = isChecked) else task
            }
            current.copy(
                tasks = updatedTasks,
                completedCount = updatedTasks.count { it.isChecked },
            )
        }
    }
}
