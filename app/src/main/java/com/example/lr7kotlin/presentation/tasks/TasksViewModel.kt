package com.example.lr7kotlin.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.domain.usecase.AddTaskUseCase
import com.example.lr7kotlin.domain.usecase.GetTasksUseCase
import com.example.lr7kotlin.di.qualifier.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getTasksUseCase().onSuccess { tasks ->
                _uiState.value = _uiState.value.copy(
                    tasks = tasks,
                    isLoading = false,
                    error = null
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message
                )
            }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch(ioDispatcher) {
            val task = Task(
                id = System.currentTimeMillis().toInt(),
                title = title,
                completed = false
            )
            addTaskUseCase(task)
        }
    }
}