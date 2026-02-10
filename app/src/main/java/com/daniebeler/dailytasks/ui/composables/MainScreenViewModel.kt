package com.daniebeler.dailytasks.ui.composables

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        loadData()
    }

    fun loadData() {
        // Use viewModelScope: tied to ViewModel lifecycle
        viewModelScope.launch {
            listToday.value = taskRepository.getTasksOfToday()
            listTomorrow.value = taskRepository.getTasksOfTomorrow()
            listOld.value = taskRepository.getExpiredTasks()
        }
    }

    fun storeNewTask(task: Task) {
        viewModelScope.launch {
            taskRepository.storeTask(task)
            loadData()
        }
    }

    fun updateTask(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(id, isCompleted)
            loadData()
        }
    }

    /**
     * Optimistic Update: Updates the UI state immediately so typing feels lag-free,
     * then saves to the database in the background.
     */
    fun updateTaskText(id: Long, newText: String) {
        // 1. Instant UI update
        listTomorrow.value = listTomorrow.value.map {
            if (it.id == id) it.copy(name = newText) else it
        }

        // 2. Background DB save
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTaskText(id, newText)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            taskRepository.deleteTask(id)
            loadData()
        }
    }

    fun moveTask(fromIndex: Int, toIndex: Int) {
        val currentList = listTomorrow.value.toMutableList()
        if (fromIndex !in currentList.indices || toIndex !in currentList.indices) return

        val item = currentList.removeAt(fromIndex)
        currentList.add(toIndex, item)

        // Update local state for the reorder animation
        listTomorrow.value = currentList

        // Persist order if your DB supports a 'priority' or 'position' column
        // viewModelScope.launch(Dispatchers.IO) { taskRepository.updateOrder(currentList) }
    }

}