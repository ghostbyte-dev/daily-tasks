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

    suspend fun getExpiredTasks(): List<Task> {
        return taskDao.getExpiredTasks(LocalDate.now().toEpochDay())
    }

    suspend fun storeTask(task: Task) {
        return taskDao.insertTask(task)
    }

    suspend fun updateTask(id: Long, isCompleted: Boolean) {
        taskDao.updateTask(id, isCompleted, LocalDate.now().toEpochDay())
    }

    suspend fun updateTaskText(id: Long, text: String) {
        taskDao.updateTaskText(id, text, LocalDate.now().toEpochDay())
    }

    suspend fun deleteTask(id: Long) {
        val taskToDelete = taskDao.getTaskById(id)
        taskToDelete?.let {
            taskDao.deleteTask(taskToDelete)
        }
    }
}