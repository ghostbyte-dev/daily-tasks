package com.daniebeler.dailytasks.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.daniebeler.dailytasks.utils.DB_VERSION

@Database(
    entities = [Task::class],
    version = DB_VERSION,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)]

)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}