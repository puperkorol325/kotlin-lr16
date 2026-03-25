package com.example.app.presentation.tasks

import android.util.Log
import androidx.lifecycle.ViewModel import androidx.lifecycle.viewModelScope
import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.domain.usecase.AddTaskUseCase
import com.example.lr7kotlin.domain.usecase.GetTasksUseCase
import com.example.lr7kotlin.presentation.tasks.TasksUiState
import kotlinx.coroutines.flow.MutableStateFlow import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow import kotlinx.coroutines.flow.update import kotlinx.coroutines.launch

class TasksViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = TasksUiState(isLoading = true)
            try {
                val result = getTasksUseCase()

                result.onSuccess { tasks ->
                    _uiState.value = TasksUiState(tasks = tasks)
                }.onFailure { error ->
                    _uiState.value = TasksUiState(error = error.message ?: "Ошибка загрузки")
                }
            } catch (e: Exception) {
                _uiState.value = TasksUiState(error = e.message ?: "Ошибка")
            }
        }
    }

    fun addTask(title: String, completed: Boolean) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val task = Task(id = 0, title = title, completed = completed)
            addTaskUseCase(task)
                .onSuccess { loadTasks() }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message ?: "Ошибка добавления") }
                }
        }
    }
}
