package com.daniebeler.dailytasks.ui.composables

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.di.TaskItem
import com.daniebeler.dailytasks.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    var listToday = mutableStateOf<List<TaskItem>>(emptyList())
        private set
    var listTomorrow = mutableStateOf<List<TaskItem>>(emptyList())
        private set
    var listOld = mutableStateOf<List<Task>>(emptyList())
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            listToday.value =
                taskRepository.getTasksOfToday().sortedBy { it.orderNumber }.map {
                    TaskItem.SavedTask(it)
                }
            listTomorrow.value =
                taskRepository.getTasksOfTomorrow().sortedBy { it.orderNumber }.map {
                    TaskItem.SavedTask(it)
                }
            addTaskPlaceholder()
            listOld.value = taskRepository.getExpiredTasks()
        }
    }

    fun addTaskPlaceholder() {
        /*val lenght = listTomorrow.value.size
        var placeholderNeededCount = 6 - lenght;
        if (placeholderNeededCount < 1) {
            placeholderNeededCount = 1;
        }
        for (i in 1..placeholderNeededCount) {
            listTomorrow.value += TaskItem.PlaceholderTask("")
        }*/
        listTomorrow.value += TaskItem.PlaceholderTask("")
        listToday.value += TaskItem.PlaceholderTask("")
    }

    fun storeNewTask(task: Task) {
        viewModelScope.launch {
            taskRepository.storeTask(task)
            loadData()
        }
    }

    fun updateTaskName(item: TaskItem, newName: String, tomorrow: Boolean) {
        val list: MutableState<List<TaskItem>> = if (tomorrow) listTomorrow else listToday

        list.value = list.value.map { current ->
            if (current === item) {
                when (current) {
                    is TaskItem.PlaceholderTask -> current.copy(name = newName)
                    is TaskItem.SavedTask -> current.copy(task = current.task.copy(name = newName))
                }
            } else {
                current
            }
        }

        when (item) {
            is TaskItem.PlaceholderTask -> {
                if (newName.length == 1) {
                    createTask(
                        item, newName, date = if(tomorrow) LocalDate.now().plusDays(1) else LocalDate.now(),
                        listState = list
                    )
                }
            }

            is TaskItem.SavedTask -> {
                viewModelScope.launch(Dispatchers.IO) {
                    taskRepository.updateTaskText(item.task.id, newName)
                }
            }
        }
    }

    private fun createTask(
        placeholder: TaskItem.PlaceholderTask,
        text: String,
        date: LocalDate,
        listState: MutableState<List<TaskItem>>
    ) {
        viewModelScope.launch {
            val epochDay = date.toEpochDay()
            val newTask = Task(
                id = 0,
                date = epochDay,
                lastInteracted = epochDay,
                name = text,
                isCompleted = false,
                orderNumber = listState.value.size
            )

            val newId = taskRepository.storeTask(newTask)
            val savedTaskEntity = newTask.copy(id = newId)

            listState.value = listState.value.map { current ->
                if (current.stableId == placeholder.stableId) {
                    TaskItem.SavedTask(
                        task = savedTaskEntity,
                        stableId = placeholder.stableId
                    )
                } else {
                    current
                }
            }
            val numberOfPlaceholders = listState.value.count { it is TaskItem.PlaceholderTask }
            if (numberOfPlaceholders == 0) {
                listState.value += TaskItem.PlaceholderTask("")
            }
        }
    }

    fun updateTask(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(id, isCompleted)
            loadData()
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            taskRepository.deleteTask(id)
            loadData()
        }
    }

    fun moveTask(
        fromIndex: Int, toIndex: Int, listState: MutableState<List<TaskItem>>
    ) {
        val currentList = listState.value.toMutableList()
        if (fromIndex !in currentList.indices || toIndex !in currentList.indices) return

        val item = currentList.removeAt(fromIndex)
        currentList.add(toIndex, item)

        // Update local state for the reorder animation
        listState.value = currentList
        var index = 0
        listState.value = listState.value.map { current ->
            if (current is TaskItem.SavedTask) {
                current.task.orderNumber = index
                viewModelScope.launch(Dispatchers.IO) {
                    taskRepository.updateTaskOrder(current.task.id, current.task.orderNumber)
                }
            }
            index++
            current
        }
    }

}