package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.db.Task
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.ReorderableTaskItem(
    reorderableState: ReorderableLazyListState,
    task: Task,
    index: Int,
    isForToday: Boolean,
    viewModel: MainScreenViewModel
) {
    ReorderableItem(reorderableState, key = task.id) { isDragging ->
        Surface(
            tonalElevation = if (isDragging) 4.dp else 0.dp,
            shadowElevation = if (isDragging) 8.dp else 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            IvyLeeTaskItem(
                index = index,
                name = task.name,
                isPlaceholder = false,
                onNameChange = { viewModel.updateTaskName(task.id, it) },
                dragHandle = {
                    IconButton(
                        modifier = Modifier.draggableHandle(
                            onDragStopped = { viewModel.saveOrder(isForToday) }
                        ),
                        onClick = {}
                    ) {
                        Icon(Icons.Rounded.DragHandle, "Reorder")
                    }
                },
                isCompleted = task.isCompleted,
                deleteItem = { viewModel.deleteTask(task.id) },
                today = isForToday,
                completeItem = { viewModel.updateTask(task.id, !task.isCompleted) }
            )
        }
    }
}