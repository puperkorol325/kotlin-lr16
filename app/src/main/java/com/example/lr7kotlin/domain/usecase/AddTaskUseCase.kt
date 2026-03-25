package com.example.lr7kotlin.domain.usecase

import com.example.lr7kotlin.domain.model.Task
import com.example.lr7kotlin.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Result<Any> {
        if (task.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Заголовок задачи не может быть пустым"))
        }

        return try {
            repository.addTask(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}