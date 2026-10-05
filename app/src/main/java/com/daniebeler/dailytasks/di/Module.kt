package com.daniebeler.dailytasks.di

import android.content.Context
import androidx.room.Room
import com.daniebeler.dailytasks.db.AppDatabase
import com.daniebeler.dailytasks.db.MIGRATION_2_3
import com.daniebeler.dailytasks.db.MIGRATION_3_4
import com.daniebeler.dailytasks.db.RoutineDao
import com.daniebeler.dailytasks.db.TaskDao
import com.daniebeler.dailytasks.utils.DB_NAME
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context, AppDatabase::class.java, DB_NAME
        ).addMigrations(MIGRATION_2_3, MIGRATION_3_4).build()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideRoutineDao(db: AppDatabase): RoutineDao = db.routineDao()
}