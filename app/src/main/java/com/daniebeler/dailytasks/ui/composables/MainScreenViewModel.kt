package com.daniebeler.dailytasks.ui.composables

import androidx.compose.runtime.mutableStateOf
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

    var listToday = mutableStateOf<List<Task>>(emptyList())
        private set
    var listTomorrow = mutableStateOf<List<Task>>(emptyList())
        private set
    var listOld = mutableStateOf<List<Task>>(emptyList())
        private set

    init {
        CoroutineScope(Dispatchers.Default).launch {
            taskRepository.storeTask(Task(0, LocalDate.now().toEpochDay(), "fief", false))
            loadData()
        }
    }

    suspend fun storeNewTask(task: Task) {
        taskRepository.storeTask(task)
        loadData()
    }

    suspend fun updateTask(id: Long, isCompleted: Boolean) {
        taskRepository.updateTask(id, isCompleted)
        loadData()
    }

    fun deleteTask(id: Long) {
        CoroutineScope(Dispatchers.Default).launch {
            taskRepository.deleteTask(id)
            loadData()
        }
    }

    private suspend fun loadData() {
        listToday.value = taskRepository.getTasksOfToday()
        listTomorrow.value = taskRepository.getTasksOfTomorrow()
    }

}