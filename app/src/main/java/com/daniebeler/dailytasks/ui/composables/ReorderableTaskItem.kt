package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.db.Task
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.ReorderableTaskItem(
    reorderableState: ReorderableLazyListState,
    task: Task,
    index: Int,
    count: Int,
    isForToday: Boolean,
    viewModel: MainScreenViewModel
) {
    var showEdit by rememberSaveable { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    ReorderableItem(reorderableState, key = task.id) { isDragging ->
        Surface(
            tonalElevation = if (isDragging) 4.dp else 0.dp,
            shadowElevation = if (isDragging) 8.dp else 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .longPressDraggableHandle(
                    onDragStarted = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                    onDragStopped = { viewModel.saveOrder(isForToday) })
        ) {
            IvyLeeTaskItem(
                index = index,
                count = count,
                name = task.name,
                onClick = { showEdit = true },
                isCompleted = task.isCompleted,
                today = isForToday,
                completeItem = { viewModel.updateTask(task.id, !task.isCompleted) })
        }
    }

    if (showEdit) {
        EditTaskBottomSheet(
            initialName = task.name,
            onDismiss = { showEdit = false },
            onSave = { viewModel.updateTaskName(task.id, it) },
            onDelete = { viewModel.deleteTask(task.id) })
    }
}