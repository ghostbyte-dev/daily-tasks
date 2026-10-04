package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun TodayPage(
    viewModel: MainScreenViewModel, modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val tasks by viewModel.listToday
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.moveTask(from.index, to.index, isForToday = true)
    }

    var showNewTask by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp)) {
        TodayHeader()

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            if (tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 56.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No tasks for today")
                    }
                }
            } else {
                itemsIndexed(
                    items = tasks, key = { _, task -> task.id }) { index, task ->
                    ReorderableTaskItem(
                        reorderableState = reorderableState,
                        task = task,
                        index = index,
                        count = tasks.size,
                        isForToday = true,
                        viewModel = viewModel
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { showNewTask = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("New Task")
                    }
                }
            }
        }
    }

    if (showNewTask) {
        NewTaskBottomSheet(
            isForToday = true,
            onDismiss = { showNewTask = false },
            onSave = { viewModel.addTask(it, isForToday = true) })
    }
}