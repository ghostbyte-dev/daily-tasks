package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
fun TomorrowPage(
    viewModel: MainScreenViewModel, modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val tasks by viewModel.listTomorrow
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.moveTask(from.index, to.index, isForToday = false)
    }

    var showNewTask by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp)) {
        TomorrowHeader()

        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp)
        ) {
            itemsIndexed(
                items = tasks, key = { _, task -> task.id }) { index, task ->
                ReorderableTaskItem(
                    reorderableState = reorderableState,
                    task = task,
                    index = index,
                    count = tasks.size,
                    isForToday = false,
                    viewModel = viewModel
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val size = ButtonDefaults.MediumContainerHeight
                    Button(
                        modifier = Modifier.heightIn(size),
                        contentPadding = ButtonDefaults.contentPaddingFor(
                            size, hasStartIcon = true
                        ),
                        onClick = { showNewTask = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.iconSizeFor(size))
                        )
                        Spacer(Modifier.size(ButtonDefaults.iconSpacingFor(size)))
                        Text(text = "New Task", style = ButtonDefaults.textStyleFor(size))
                    }
                }
            }
        }
    }

    if (showNewTask) {
        NewTaskBottomSheet(
            isForToday = false,
            onDismiss = { showNewTask = false },
            onSave = { viewModel.addTask(it, isForToday = false) })
    }
}