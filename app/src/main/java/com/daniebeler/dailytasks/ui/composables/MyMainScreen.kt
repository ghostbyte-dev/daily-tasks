package com.daniebeler.dailytasks.ui.composables

import IvyLeeTaskItem
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.daniebeler.dailytasks.R
import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.ui.theme.MyVariableFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
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

    val showBottomSheet = remember { mutableStateOf(false) }

    NewTaskBottomSheet(
        showState = showBottomSheet,
        isForToday = pagerState.currentPage == 0,
        storeTask = { taskText ->

            var date = LocalDate.now().toEpochDay()
            if (pagerState.currentPage == 1) {
                date++
            }
            val newTask = Task(0, date, date, taskText, false)

            CoroutineScope(Dispatchers.Default).launch {
                viewModel.storeNewTask(newTask)
            }

            showBottomSheet.value = false
        })

    Scaffold(content = { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(
                Modifier.fillMaxSize()
            ) {
                //IvyLeeRow()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PrimaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.widthIn(max = 300.dp),
                        divider = {},
                    ) {
                        val tabs = listOf(
                            stringResource(R.string.today) to 0,
                            stringResource(R.string.tomorrow) to 1
                        )

                        tabs.forEach { (title, index) ->
                            val isSelected = pagerState.currentPage == index
                            val animatedWeight by animateIntAsState(
                                targetValue = if (isSelected) 700 else 400,
                                animationSpec = spring(stiffness = Spring.StiffnessLow),
                                label = "FontWeightAnimation"
                            )

                            val textColor by animateColorAsState(
                                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                label = "TabColor"
                            )


                            Tab(
                                selected = isSelected,
                                onClick = {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                },
                                text = {
                                    Text(
                                        text = title,
                                        style = TextStyle(
                                            fontFamily = MyVariableFont,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(animatedWeight)
                                        ),
                                        color = textColor
                                    )
                                }
                            )
                        }

                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) { tabIndex ->
                    when (tabIndex) {
                        0 -> Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            if (viewModel.listToday.value.isEmpty() && viewModel.listOld.value.isEmpty()) {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(64.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            } else {
                                LazyColumn(contentPadding = PaddingValues(top = 12.dp)) {
                                    items(viewModel.listOld.value + viewModel.listToday.value) { listElement ->
                                        TodoItem(listElement, updateTask = { isCompleted ->
                                            CoroutineScope(Dispatchers.Default).launch {
                                                viewModel.updateTask(
                                                    listElement.id, isCompleted = isCompleted
                                                )
                                            }
                                        }, updateText = { text ->
                                            CoroutineScope(Dispatchers.Default).launch {
                                                viewModel.updateTaskText(
                                                    listElement.id, newText = text
                                                )
                                            }
                                        }, deleteTask = {
                                            CoroutineScope(Dispatchers.Default).launch {
                                                viewModel.deleteTask(
                                                    listElement.id
                                                )
                                            }
                                        })
                                    }
                                }

                            }
                        }

                        1 -> {
                            val lazyListState = rememberLazyListState()
                            val tomorrowTasks = viewModel.listTomorrow.value
                            val reorderableState =
                                rememberReorderableLazyListState(lazyListState) { from, to ->
                                    viewModel.moveTask(from.index, to.index)
                                }

                            LazyColumn(
                                state = lazyListState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(top = 12.dp)
                            ) {

                                itemsIndexed(
                                    tomorrowTasks,
                                    key = { _, task -> task.id }) { index, task ->
                                    ReorderableItem(reorderableState, key = task.id) { isDragging ->
                                        // Apply a surface or background so the item looks solid when dragged
                                        Surface(
                                            tonalElevation = if (isDragging) 4.dp else 0.dp,
                                            shadowElevation = if (isDragging) 8.dp else 0.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            IvyLeeTaskItem(
                                                index = index,
                                                name = task.name,
                                                onNameChange = {
                                                    viewModel.updateTaskText(
                                                        task.id,
                                                        it
                                                    )
                                                },
                                                dragHandle = {
                                                    IconButton(
                                                        modifier = Modifier.draggableHandle(),
                                                        onClick = {}) {
                                                        Icon(Icons.Rounded.DragHandle, "Reorder")
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }

                                val remainingSlotsCount = 6 - tomorrowTasks.size
                                items(remainingSlotsCount) { i ->
                                    val slotIndex = tomorrowTasks.size + i
                                    IvyLeeTaskItem(
                                        index = slotIndex,
                                        name = "", // Always empty for placeholder slots
                                        isPlaceholder = true,
                                        onNameChange = { typedText ->
                                            if (typedText.isNotEmpty()) {
                                                viewModel.newTaskTomorrow(typedText)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                NewTaskButtonRow {
                    showBottomSheet.value = true
                }
            }
        }
    })
}