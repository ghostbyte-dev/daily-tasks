package com.daniebeler.dailytasks.repository

import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.db.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    suspend fun getTasksOfToday(): List<Task> {
        return taskDao.getTasksOfToday()
    }

    suspend fun getTasksOfTomorrow(): List<Task> {
        return taskDao.getTasksOfTomorrow()
    }

    suspend fun storeTask(task: Task) {
        return taskDao.insertTask(task)
    }
}