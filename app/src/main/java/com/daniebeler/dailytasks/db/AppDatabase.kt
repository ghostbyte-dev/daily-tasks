package com.daniebeler.dailytasks.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daniebeler.dailytasks.utils.DB_VERSION

@Database(entities = [Task::class], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}