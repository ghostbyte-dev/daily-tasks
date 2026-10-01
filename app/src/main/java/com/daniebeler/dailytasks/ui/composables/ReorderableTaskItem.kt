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
import com.daniebeler.dailytasks.di.TaskItem
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.ReorderableTaskItem(
    reorderableState: ReorderableLazyListState,
    item: TaskItem,
    index: Int,
    isTomorrow: Boolean,
    viewModel: MainScreenViewModel
) {
    ReorderableItem(reorderableState, key = item.stableId) { isDragging ->
        Surface(
            tonalElevation = if (isDragging) 4.dp else 0.dp,
            shadowElevation = if (isDragging) 8.dp else 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            IvyLeeTaskItem(
                index = index,
                name = when (item) {
                    is TaskItem.SavedTask -> item.task.name
                    is TaskItem.PlaceholderTask -> item.name
                },
                isPlaceholder = item is TaskItem.PlaceholderTask,
                onNameChange = { viewModel.updateTaskName(item, it, isTomorrow) },
                dragHandle = {
                    IconButton(
                        modifier = Modifier.draggableHandle(),
                        onClick = {}
                    ) {
                        Icon(Icons.Rounded.DragHandle, "Reorder")
                    }
                },
                isCompleted = when (item) {
                    is TaskItem.SavedTask -> item.task.isCompleted
                    is TaskItem.PlaceholderTask -> true
                },
                deleteItem = {
                    if (item is TaskItem.SavedTask) viewModel.deleteTask(item.task.id)
                },
                today = !isTomorrow,
                completeItem = {
                    if (item is TaskItem.SavedTask) {
                        viewModel.updateTask(item.task.id, !item.task.isCompleted)
                    }
                }
            )
        }
    }
}