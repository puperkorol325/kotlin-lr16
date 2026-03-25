package com.example.lr7kotlin.domain.usecase

import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): Result<List<Task>> {
        return try {
            repository.getTasks()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
