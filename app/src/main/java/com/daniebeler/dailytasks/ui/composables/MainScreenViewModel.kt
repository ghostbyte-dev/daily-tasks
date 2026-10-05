package com.daniebeler.dailytasks.ui.composables

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.dailytasks.db.Routine
import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.db.isDueOn
import com.daniebeler.dailytasks.repository.RoutineRepository
import com.daniebeler.dailytasks.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository, private val routineRepository: RoutineRepository
) : ViewModel() {

    var listToday = mutableStateOf<List<Task>>(emptyList())
        private set
    var listTomorrow = mutableStateOf<List<Task>>(emptyList())
        private set
    var listOld = mutableStateOf<List<Task>>(emptyList())
        private set

    var routines = mutableStateOf<List<Routine>>(emptyList())
        private set

    private val generateMutex = Mutex()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            generateDueRoutines()
            listToday.value = taskRepository.getTasksOfToday().sortedBy { it.orderNumber }
            listTomorrow.value = taskRepository.getTasksOfTomorrow().sortedBy { it.orderNumber }
            listOld.value = taskRepository.getExpiredTasks()
            routines.value = routineRepository.getAll()
        }
    }

    /** Creates today's task for every routine that is due and hasn't been generated yet. */
    private suspend fun generateDueRoutines() = generateMutex.withLock {
        val today = LocalDate.now().toEpochDay()
        var nextOrder = taskRepository.getTasksOfToday().size
        routineRepository.getAll().forEach { routine ->
            if (routine.lastGeneratedDate < today && routine.isDueOn(today)) {
                routineRepository.markGenerated(routine.id, today)
                taskRepository.storeTask(
                    Task(
                        id = 0,
                        date = today,
                        lastInteracted = today,
                        name = routine.name,
                        isCompleted = false,
                        orderNumber = nextOrder++,
                        routineId = routine.id
                    )
                )
            }
        }
    }

    private fun listFor(isForToday: Boolean): MutableState<List<Task>> =
        if (isForToday) listToday else listTomorrow

    fun addTask(text: String, isForToday: Boolean) {
        val date = if (isForToday) LocalDate.now() else LocalDate.now().plusDays(1)
        val list = if (isForToday) listToday else listTomorrow
        viewModelScope.launch {
            val epochDay = date.toEpochDay()
            val task = Task(
                id = 0,
                date = epochDay,
                lastInteracted = epochDay,
                name = text,
                isCompleted = false,
                orderNumber = list.value.size
            )
            val id = taskRepository.storeTask(task)
            list.value += task.copy(id = id)
        }
    }

    /** Applies [transform] to the task with [id] in whichever list contains it. */
    private fun modifyTask(id: Long, transform: (Task) -> Task) {
        listOf(listToday, listTomorrow).forEach { list ->
            list.value = list.value.map { if (it.id == id) transform(it) else it }
        }
    }

    fun updateTask(id: Long, isCompleted: Boolean) {
        modifyTask(id) { it.copy(isCompleted = isCompleted) }
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(id, isCompleted)
        }
    }

    fun updateTaskName(id: Long, newName: String) {
        modifyTask(id) { it.copy(name = newName) }
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTaskText(id, newName)
        }
    }

    fun deleteTask(id: Long) {
        listOf(listToday, listTomorrow).forEach { list ->
            val remaining = list.value.filter { it.id != id }
            if (remaining.size != list.value.size) {
                // Close the gap in the order numbers
                list.value = remaining.mapIndexed { i, t -> t.copy(orderNumber = i) }
                saveOrder(list.value)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(id)
        }
    }

    /** In-memory only. Call from the reorder callback on every move. */
    fun moveTask(from: Int, to: Int, isForToday: Boolean) {
        val list = listFor(isForToday)
        val current = list.value.toMutableList()
        if (from !in current.indices || to !in current.indices) return
        current.add(to, current.removeAt(from))
        list.value = current.mapIndexed { i, t -> t.copy(orderNumber = i) }
    }

    /** Persists the current order. Call once when the drag ends. */
    fun saveOrder(isForToday: Boolean) = saveOrder(listFor(isForToday).value)

    private fun saveOrder(tasks: List<Task>) {
        viewModelScope.launch(Dispatchers.IO) {
            tasks.forEach { taskRepository.updateTaskOrder(it.id, it.orderNumber) }
        }
    }

    fun addRoutine(name: String, intervalDays: Int) {
        viewModelScope.launch {
            val tomorrow = LocalDate.now().plusDays(1).toEpochDay()
            routineRepository.add(
                Routine(name = name, intervalDays = intervalDays, startDate = tomorrow)
            )
            routines.value = routineRepository.getAll()
        }
    }

    fun updateRoutine(id: Long, name: String, intervalDays: Int) {
        viewModelScope.launch {
            routineRepository.update(id, name, intervalDays)
            routines.value = routineRepository.getAll()
        }
    }

    fun deleteRoutine(id: Long) {
        viewModelScope.launch {
            routineRepository.delete(id)
            routines.value = routineRepository.getAll()
        }
    }
}