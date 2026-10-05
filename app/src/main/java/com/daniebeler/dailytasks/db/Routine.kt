package com.daniebeler.dailytasks.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.daniebeler.dailytasks.utils.TABLE_ROUTINES

@Entity(tableName = TABLE_ROUTINES)
data class Routine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val intervalDays: Int,          // 1 = every day, 7 = weekly, ...
    val startDate: Long,            // epoch day of the first occurrence
    val lastGeneratedDate: Long = 0 // epoch day a task was last created for
)

fun Routine.isDueOn(day: Long): Boolean =
    day >= startDate && (day - startDate) % intervalDays == 0L

fun Routine.intervalLabel(): String =
    if (intervalDays == 1) "Every day" else "Every $intervalDays days"

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: Routine): Long

    @Query("SELECT * FROM $TABLE_ROUTINES")
    suspend fun getAll(): List<Routine>

    @Query("UPDATE $TABLE_ROUTINES SET name = :name, intervalDays = :intervalDays WHERE id = :id")
    suspend fun update(id: Long, name: String, intervalDays: Int)

    @Query("UPDATE $TABLE_ROUTINES SET lastGeneratedDate = :day WHERE id = :id")
    suspend fun markGenerated(id: Long, day: Long)

    @Query("DELETE FROM $TABLE_ROUTINES WHERE id = :id")
    suspend fun delete(id: Long)
}