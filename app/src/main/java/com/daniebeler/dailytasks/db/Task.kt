package com.daniebeler.dailytasks.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val name: String,
    val isCompleted: Boolean
)