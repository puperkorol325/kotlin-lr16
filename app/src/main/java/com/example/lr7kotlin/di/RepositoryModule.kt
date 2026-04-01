package com.example.lr7kotlin.di

import com.example.lr7kotlin.data.local.LocalTaskDataSource
import com.example.lr7kotlin.data.local.TaskDao
import com.example.lr7kotlin.data.remote.RemoteTaskDataSource
import com.example.lr7kotlin.data.remote.api.TaskApi
import com.example.lr7kotlin.di.qualifier.LocalDataSource
import com.example.lr7kotlin.di.qualifier.RemoteDataSource
import com.example.lr7kotlin.domain.repository.TaskRepository
import com.example.lr7kotlin.data.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    @LocalDataSource
    fun provideLocalTaskDataSource(taskDao: TaskDao): LocalTaskDataSource =
        LocalTaskDataSource(taskDao)

    @Provides
    @Singleton
    @RemoteDataSource
    fun provideRemoteTaskDataSource(taskApi: TaskApi): RemoteTaskDataSource =
        RemoteTaskDataSource(taskApi)
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        @LocalDataSource localDataSource: LocalTaskDataSource,
        @RemoteDataSource remoteDataSource: RemoteTaskDataSource
    ): TaskRepository = TaskRepositoryImpl(localDataSource, remoteDataSource)
}