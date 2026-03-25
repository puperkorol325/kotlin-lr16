package com.example.lr7kotlin.data.repository

import com.example.lr7kotlin.data.remote.api.TaskApi
import com.example.lr7kotlin.data.remote.mapper.toDomain
import com.example.lr7kotlin.data.remote.mapper.toDto
import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class TaskRepositoryImpl(
    private val api: TaskApi
) : TaskRepository {

    override suspend fun getTasks(): Result<List<Task>> = withContext(Dispatchers.IO) {
        try {
            val dtos = api.getTasks()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: IOException) { Result.failure(e)

        }
    }

    override suspend fun getTaskById(id: Int): Result<Task?> = withContext(Dispatchers.IO) {
        try {
            val dto = api.getTaskById(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTask(task: Task): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.addTask(task.toDto())
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.updateTask(task.id, task.toDto())
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)

        }
    }

    override suspend fun deleteTask(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.deleteTask(id)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}
