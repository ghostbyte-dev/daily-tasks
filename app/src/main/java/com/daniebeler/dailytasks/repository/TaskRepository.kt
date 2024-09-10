package com.daniebeler.dailytasks.repository

import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.db.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }
}