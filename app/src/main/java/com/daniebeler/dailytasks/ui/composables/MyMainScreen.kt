package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.daniebeler.dailytasks.di.TaskItem
import com.daniebeler.dailytasks.utils.imeAwareInsets
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private data class ToolbarDestination(val label: String, val icon: ImageVector)


@Composable
fun MyMainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(
        checkNotNull(
            LocalViewModelStoreOwner.current
        ) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }, "12"
    )
) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState { 2 }

    val destinations = listOf(
        ToolbarDestination("Today", Icons.Rounded.Today),
        ToolbarDestination("Tomorrow", Icons.Rounded.Event)
    )

    Scaffold(content = { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .imeAwareInsets()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) { tabIndex ->
                    when (tabIndex) {
                        0 -> {
                            val lazyListState = rememberLazyListState()
                            val tomorrowTasks = viewModel.listToday.value
                            val reorderableState =
                                rememberReorderableLazyListState(lazyListState) { from, to ->
                                    viewModel.moveTask(from.index, to.index, viewModel.listToday)
                                }

                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(top = 12.dp)
                            ) {

                                itemsIndexed(
                                    items = tomorrowTasks,
                                    key = { _, item -> item.stableId }) { index, item ->
                                    ReorderableItem(
                                        reorderableState,
                                        key = item.stableId,
                                    ) { isDragging ->
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
                                                onNameChange = {
                                                    viewModel.updateTaskName(item, it, false)
                                                },
                                                dragHandle = {
                                                    IconButton(
                                                        modifier = Modifier.draggableHandle(),
                                                        onClick = {}) {
                                                        Icon(Icons.Rounded.DragHandle, "Reorder")
                                                    }
                                                },
                                                isCompleted = when (item) {
                                                    is TaskItem.SavedTask -> item.task.isCompleted
                                                    is TaskItem.PlaceholderTask -> true
                                                },
                                                deleteItem = {
                                                    when (item) {
                                                        is TaskItem.SavedTask -> viewModel.deleteTask(
                                                            item.task.id
                                                        )

                                                        is TaskItem.PlaceholderTask -> {}
                                                    }
                                                },
                                                today = true,
                                                completeItem = {
                                                    when (item) {
                                                        is TaskItem.SavedTask -> viewModel.updateTask(
                                                            item.task.id, !item.task.isCompleted
                                                        )

                                                        is TaskItem.PlaceholderTask -> {}
                                                    }
                                                })
                                        }
                                    }
                                }
                            }
                        }

                        1 -> {
                            val lazyListState = rememberLazyListState()
                            val tomorrowTasks = viewModel.listTomorrow.value
                            val reorderableState =
                                rememberReorderableLazyListState(lazyListState) { from, to ->
                                    viewModel.moveTask(from.index, to.index, viewModel.listTomorrow)
                                }

                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(top = 12.dp)
                            ) {

                                itemsIndexed(
                                    items = tomorrowTasks,
                                    key = { _, item -> item.stableId }) { index, item ->
                                    ReorderableItem(
                                        reorderableState,
                                        key = item.stableId,
                                    ) { isDragging ->
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
                                                onNameChange = {
                                                    viewModel.updateTaskName(item, it, true)
                                                },
                                                dragHandle = {
                                                    IconButton(
                                                        modifier = Modifier.draggableHandle(),
                                                        onClick = {}) {
                                                        Icon(Icons.Rounded.DragHandle, "Reorder")
                                                    }
                                                },
                                                isCompleted = when (item) {
                                                    is TaskItem.SavedTask -> item.task.isCompleted
                                                    is TaskItem.PlaceholderTask -> true
                                                },
                                                deleteItem = {
                                                    when (item) {
                                                        is TaskItem.SavedTask -> viewModel.deleteTask(
                                                            item.task.id
                                                        )

                                                        is TaskItem.PlaceholderTask -> {}
                                                    }
                                                },
                                                today = false,
                                                completeItem = {
                                                    when (item) {
                                                        is TaskItem.SavedTask -> viewModel.updateTask(
                                                            item.task.id, !item.task.isCompleted
                                                        )

                                                        is TaskItem.PlaceholderTask -> {}
                                                    }
                                                })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                val systemNavigationBarHeight =
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

                HorizontalFloatingToolbar(
                    expanded = true,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = systemNavigationBarHeight + 4.dp)
                ) {
                    destinations.forEachIndexed { index, destination ->
                        val isActive = pagerState.currentPage == index

                        if (isActive) {
                            Button(
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                }) {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(destination.label)
                            }
                        } else {
                            TextButton(
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                }) {
                                Text(destination.label)
                            }
                        }
                    }
                }
            }
        }
    })
}