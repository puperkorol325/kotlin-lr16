package com.example.lr7kotlin.data.remote

import com.example.lr7kotlin.data.remote.api.TaskApi
import com.example.lr7kotlin.data.remote.mapper.toDomain
import com.example.lr7kotlin.data.remote.mapper.toDto
import com.example.lr7kotlin.domain.model.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteTaskDataSource @Inject constructor(
    private val taskApi: TaskApi
) {
    suspend fun getAllTasks(): Result<List<Task>> = try {
        val dtos = taskApi.getTasks()
        Result.success(dtos.map { it.toDomain() })
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getTaskById(id: Int): Result<Task?> = try {
        val dto = taskApi.getTaskById(id)
        Result.success(dto.toDomain())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun addTask(task: Task): Result<Task> = try {
        val dto = taskApi.addTask(task.toDto())
        Result.success(dto.toDomain())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateTask(id: Int, task: Task): Result<Task> = try {
        val dto = taskApi.updateTask(id, task.toDto())
        Result.success(dto.toDomain())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteTask(id: Int): Result<Unit> = try {
        taskApi.deleteTask(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}