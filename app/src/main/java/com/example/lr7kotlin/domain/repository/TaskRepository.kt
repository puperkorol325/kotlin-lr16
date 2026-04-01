package com.example.lr7kotlin.domain.repository

import com.example.lr7kotlin.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getTasks(): Result<List<Task>>
    fun getTasksFlow(): Flow<List<Task>>
    suspend fun getTaskById(id: Int): Result<Task?>
    suspend fun addTask(task: Task): Result<Unit>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(id: Int): Result<Unit>
}