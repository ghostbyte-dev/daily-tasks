package com.daniebeler.dailytasks.ui.composables

import androidx.lifecycle.ViewModel
import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    var listToday = mutableListOf<Task>()
    var listTomorrow = mutableListOf<Task>()
    var listOld = mutableListOf<Task>()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            taskRepository.storeTask(Task(0, LocalDate.now().toEpochDay(), "fief", false))
            listToday = taskRepository.getTasksOfToday().toMutableList()
            listTomorrow = taskRepository.getTasksOfTomorrow().toMutableList()
        }
    }

    suspend fun storeNewTask(task: Task) {
        taskRepository.storeTask(task)
    }

    fun updateTask(id: Long, isCompleted: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {
            taskRepository.updateTask(id, isCompleted)
            listToday.find { it.id == id }?.isCompleted = isCompleted
            listTomorrow.find { it.id == id }?.isCompleted = isCompleted
        }
    }

    fun loadData() {
        CoroutineScope(Dispatchers.Default).launch {
            listToday = taskRepository.getTasksOfToday().toMutableList()
            listTomorrow = taskRepository.getTasksOfTomorrow().toMutableList()
        }
    }

}