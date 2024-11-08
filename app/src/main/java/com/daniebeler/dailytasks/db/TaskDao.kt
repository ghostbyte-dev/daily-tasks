package com.daniebeler.dailytasks.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)


    @Query("SELECT * FROM tasks WHERE date = :day")
    suspend fun getTasksFromDay(day: Long): List<Task>

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTask(id: Long, isCompleted: Boolean)
}