package com.example.lr7kotlin.data.local

import com.example.lr7kotlin.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalTaskDataSource @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getTaskById(id: Int): Task? =
        taskDao.getTaskById(id)?.toDomain()

    suspend fun insertTask(task: Task) =
        taskDao.insertTask(task.toEntity())

    suspend fun updateTask(task: Task) =
        taskDao.updateTask(task.toEntity())

    suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(task.toEntity())

    suspend fun deleteTaskById(id: Int) =
        taskDao.deleteTaskById(id)
}