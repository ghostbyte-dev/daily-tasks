package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun TomorrowPage(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val tasks = viewModel.listTomorrow.value
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.moveTask(from.index, to.index, viewModel.listTomorrow)
    }

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
                items = tasks,
                key = { _, item -> item.stableId }
            ) { index, item ->
                ReorderableTaskItem(
                    reorderableState = reorderableState,
                    item = item,
                    index = index,
                    isTomorrow = true,
                    viewModel = viewModel
                )
            }
        }
    }
}