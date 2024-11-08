package com.daniebeler.dailytasks.repository

import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.db.TaskDao
import java.time.LocalDate
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    suspend fun getTasksOfToday(): List<Task> {
        return taskDao.getTasksFromDay(LocalDate.now().toEpochDay())
    }

    suspend fun getTasksOfTomorrow(): List<Task> {
        return taskDao.getTasksFromDay(LocalDate.now().toEpochDay().plus(1))
    }

    suspend fun storeTask(task: Task) {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(id: Long, isCompleted: Boolean) {
        taskDao.updateTask(id, isCompleted)
    }
}