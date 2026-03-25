package com.example.lr7kotlin.data.remote.mapper


import com.example.lr7kotlin.data.remote.dto.TaskDto
import com.example.lr7kotlin.domain.model.Task

fun TaskDto.toDomain(): Task = Task(
    id = id,
    title = title,
    completed = completed
)

fun Task.toDto(): TaskDto = TaskDto(
    id = id,
    title = title,
    completed = completed
)
