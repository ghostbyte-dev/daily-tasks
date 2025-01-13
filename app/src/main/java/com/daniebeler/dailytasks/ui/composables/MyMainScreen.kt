package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.dailytasks.R
import com.daniebeler.dailytasks.db.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMainScreen(viewModel: MainScreenViewModel = hiltViewModel(key = "12")) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState { 2 }

    val showBottomSheet = remember { mutableStateOf(false) }

    NewTaskBottomSheet(showState = showBottomSheet, storeTask = { taskText ->

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
                IvyLeeRow()

                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage
                ) {
                    Tab(text = {
                        Text(
                            stringResource(R.string.today),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, selected = pagerState.currentPage == 0, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }

                    })

                    Tab(text = {
                        Text(
                            stringResource(R.string.tomorrow),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }, selected = pagerState.currentPage == 0, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    })
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
                                    contentDescription = "Shopping Cart",
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

                        1 -> Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            if (viewModel.listTomorrow.value.isEmpty()) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Shopping Cart",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(64.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            } else {
                                LazyColumn(contentPadding = PaddingValues(top = 12.dp)) {
                                    items(viewModel.listTomorrow.value) { listElement ->
                                        TodoItem(listElement, updateTask = { isCompleted ->
                                            CoroutineScope(Dispatchers.Default).launch {
                                                viewModel.updateTask(
                                                    listElement.id, isCompleted = isCompleted
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
                    }
                }

                NewTaskButtonRow {
                    showBottomSheet.value = true
                }
            }
        }
    })
}