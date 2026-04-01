package com.example.lr7kotlin.data.local

import android.content.Context
import androidx.room.Database  // ← правильный импорт
import androidx.room.Room     // ← правильный импорт
import androidx.room.RoomDatabase  // ← правильный импорт
import com.example.lr7kotlin.domain.model.Task

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasks_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}