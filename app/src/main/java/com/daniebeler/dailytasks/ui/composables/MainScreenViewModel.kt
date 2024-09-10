package com.daniebeler.dailytasks.ui.composables

import androidx.lifecycle.ViewModel
import com.daniebeler.dailytasks.ToDoItem
import com.daniebeler.dailytasks.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    var listToday = mutableListOf<ToDoItem>()
    var listTomorrow = mutableListOf<ToDoItem>()
    var listOld = mutableListOf<ToDoItem>()

}