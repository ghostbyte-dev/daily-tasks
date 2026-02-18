package com.daniebeler.dailytasks.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?

    @Query("SELECT * FROM tasks WHERE date = :day")
    suspend fun getTasksFromDay(day: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE (date < :today AND lastInteracted = :today AND isCompleted) OR (not isCompleted AND date < :today)")
    suspend fun getExpiredTasks(today: Long): List<Task>

    @Query("UPDATE tasks SET lastInteracted = :lastInteracted, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTask(id: Long, isCompleted: Boolean, lastInteracted: Long)

    @Query("UPDATE tasks SET lastInteracted = :lastInteracted, name = :text WHERE id = :id")
    suspend fun updateTaskText(id: Long, text: String, lastInteracted: Long)

    @Query("UPDATE tasks SET lastInteracted = :lastInteracted, orderNumber = :orderNumber WHERE id = :id")
    suspend fun updateTaskText(id: Long, orderNumber: Int, lastInteracted: Long)

    @Delete()
    suspend fun deleteTask(task: Task)
}