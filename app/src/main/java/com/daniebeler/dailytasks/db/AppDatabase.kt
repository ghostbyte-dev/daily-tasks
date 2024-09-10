package com.daniebeler.dailytasks.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//@TypeConverters(Converters::class)
@Database(entities = [Task::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}