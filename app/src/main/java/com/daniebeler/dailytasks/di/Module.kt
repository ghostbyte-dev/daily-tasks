package com.daniebeler.dailytasks.di

import android.content.Context
import androidx.room.Room
import com.daniebeler.dailytasks.db.AppDatabase
import com.daniebeler.dailytasks.db.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Module {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "task_database")
        .build()

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }
}