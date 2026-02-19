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
            val savedItemsToday: List<TaskItem.SavedTask> =
                taskRepository.getTasksOfToday().sortedBy { it.orderNumber }.map {
                    TaskItem.SavedTask(it)
                }
            val savedItemsTomorrow: List<TaskItem.SavedTask> =
                taskRepository.getTasksOfTomorrow().sortedBy { it.orderNumber }.map {
                    TaskItem.SavedTask(it)
                }
            addTaskPlaceholder(listTomorrow, savedItemsTomorrow)
            addTaskPlaceholder(listToday, savedItemsToday)
            listOld.value = taskRepository.getExpiredTasks()
        }
    }

    fun addTaskPlaceholder(
        list: MutableState<List<TaskItem>>,
        savedItems: List<TaskItem.SavedTask>
    ) {
        list.value = emptyList()
        var length = savedItems.size
        if (length <= 5) length = 5

        for (i in 0..length) {
            val savedItem = savedItems.find { it.task.orderNumber == i }
            if (savedItem == null) {
                list.value += TaskItem.PlaceholderTask("")
            } else {
                list.value += savedItem
            }
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
                        item,
                        newName,
                        date = if (tomorrow) LocalDate.now().plusDays(1) else LocalDate.now(),
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
        var orderNumber = 0
        val index = listState.value.indexOfFirst { it.stableId === placeholder.stableId }
        orderNumber += if (index != -1) {
            index
        } else {
            listState.value.size
        }
        viewModelScope.launch {
            val epochDay = date.toEpochDay()
            val newTask = Task(
                id = 0,
                date = epochDay,
                lastInteracted = epochDay,
                name = text,
                isCompleted = false,
                orderNumber = orderNumber
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