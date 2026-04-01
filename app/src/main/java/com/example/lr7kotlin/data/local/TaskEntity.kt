package com.example.lr7kotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lr7kotlin.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val completed: Boolean
)

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    completed = completed
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    completed = completed
)