package com.daniebeler.dailytasks.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniebeler.dailytasks.utils.TABLE_TASKS
import java.time.LocalDate

@Entity(tableName = TABLE_TASKS)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    @ColumnInfo(defaultValue = "0") val lastInteracted: Long,
    val name: String,
    var isCompleted: Boolean,
    @ColumnInfo(defaultValue = "0")
    var orderNumber: Int = 0
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