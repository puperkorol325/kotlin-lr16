package com.example.lr7kotlin.presentation.tasks

import com.example.lr7kotlin.domain.model.Task

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)