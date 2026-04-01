package com.example.lr7kotlin

import com.example.lr7kotlin.data.remote.api.TaskApi
import com.example.lr7kotlin.data.repository.TaskRepositoryImpl
import com.example.lr7kotlin.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(api: TaskApi): TaskRepository =
        TaskRepositoryImpl(api)
}