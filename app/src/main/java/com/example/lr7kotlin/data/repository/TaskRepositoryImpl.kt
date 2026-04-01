package com.example.lr7kotlin.data.repository

import com.example.lr7kotlin.data.local.LocalTaskDataSource
import com.example.lr7kotlin.data.remote.RemoteTaskDataSource
import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val localDataSource: LocalTaskDataSource,
    private val remoteDataSource: RemoteTaskDataSource
) : TaskRepository {

    override suspend fun getTasks(): Result<List<Task>> {
        return remoteDataSource.getAllTasks().onSuccess { tasks ->
            tasks.forEach { task ->
                localDataSource.insertTask(task)
            }
        }
    }

    override fun getTasksFlow(): Flow<List<Task>> =
        localDataSource.getAllTasks()

    override suspend fun getTaskById(id: Int): Result<Task?> =
        remoteDataSource.getTaskById(id)

    override suspend fun addTask(task: Task): Result<Unit> {
        return remoteDataSource.addTask(task).map {
            localDataSource.insertTask(it)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return remoteDataSource.updateTask(task.id, task).map {
            localDataSource.updateTask(it)
        }
    }

    override suspend fun deleteTask(id: Int): Result<Unit> {
        return remoteDataSource.deleteTask(id).onSuccess {
            localDataSource.deleteTaskById(id)
        }
    }
}