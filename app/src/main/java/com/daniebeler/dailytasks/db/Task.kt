package com.daniebeler.dailytasks.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daniebeler.dailytasks.utils.TABLE_ROUTINES
import com.daniebeler.dailytasks.utils.TABLE_TASKS
import java.time.LocalDate

@Entity(tableName = TABLE_TASKS)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    @ColumnInfo(defaultValue = "0") val lastInteracted: Long,
    val name: String,
    var isCompleted: Boolean,
    var orderNumber: Int = 0,
    val routineId: Long? = null
)

fun Task.isOverdue(): Pair<Boolean, Long> {
    val currentEpochDay = LocalDate.now().toEpochDay()
    val taskEpochDay = this.date

    return if (taskEpochDay < currentEpochDay) {
        val daysOverdue = currentEpochDay - taskEpochDay
        true to daysOverdue
    } else {
        false to 0
    }
}

fun Task.isUntilToday(): Boolean {
    val currentEpochDay = LocalDate.now().toEpochDay()
    val taskEpochDay = this.date

    return taskEpochDay == currentEpochDay
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE $TABLE_TASKS ADD COLUMN orderNumber INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE $TABLE_TASKS ADD COLUMN routineId INTEGER")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_ROUTINES (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, intervalDays INTEGER NOT NULL, startDate INTEGER NOT NULL, lastGeneratedDate INTEGER NOT NULL)"
        )
    }
}