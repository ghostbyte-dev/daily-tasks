package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daniebeler.dailytasks.R
import com.daniebeler.dailytasks.db.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MyMainScreen(viewModel: MainScreenViewModel = hiltViewModel(key = "12")) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var modalTextValue by remember {
        mutableStateOf("")
    }

    val pagerState = rememberPagerState { 2 }

    var showSheet by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    if (showSheet) {

        ModalBottomSheet(
            sheetState = sheetState,
            dragHandle = null,
            onDismissRequest = {
                focusManager.clearFocus()
                showSheet = false
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = modalTextValue,
                    onValueChange = { newText ->
                        modalTextValue = newText
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (modalTextValue.isNotBlank()) {
                            keyboardController?.hide()
                            var date = LocalDate.now().toEpochDay()
                            if (pagerState.currentPage == 1) {
                                date++
                            }
                            val newTask = Task(0, date, modalTextValue, false)

                            CoroutineScope(Dispatchers.Default).launch {
                                viewModel.storeNewTask(newTask)
                            }

                            modalTextValue = ""

                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    focusManager.clearFocus()
                                    showSheet = false
                                }
                            }
                        }
                    }),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )


                Button(onClick = {
                    if (modalTextValue.isNotBlank()) {
                        var date = LocalDate.now().toEpochDay()
                        if (pagerState.currentPage == 1) {
                            date++
                        }
                        val newTask = Task(0, date, modalTextValue, false)

                        CoroutineScope(Dispatchers.Default).launch {
                            viewModel.storeNewTask(newTask)
                        }

                        modalTextValue = ""

                        //listToday = dbHandler.getToDos("today")
                        //listTomorrow = dbHandler.getToDos("tomorrow")
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                focusManager.clearFocus()
                                showSheet = false
                            }
                        }
                    }
                }) {
                    Text(text = stringResource(R.string.save))
                }
            }

            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }
    }

    Scaffold(content = { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            Column(
                Modifier.fillMaxSize()
            ) {
                IvyLeeRow()

                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage
                ) {
                    Tab(text = { Text(stringResource(R.string.today)) },
                        selected = pagerState.currentPage == 0,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }

                        })

                    Tab(text = { Text(stringResource(R.string.tomorrow)) },
                        selected = pagerState.currentPage == 0,
                        onClick = {
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
                                .padding(16.dp)
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
                                LazyColumn {
                                    items(viewModel.listToday.value) { listElement ->
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                                .combinedClickable(onClick = {
                                                    CoroutineScope(Dispatchers.Default).launch {

                                                        viewModel.updateTask(
                                                            listElement.id, !listElement.isCompleted
                                                        )
                                                    }

                                                }, onLongClick = {
                                                    viewModel.deleteTask(listElement.id)
                                                })
                                        ) {
                                            if (listElement.isCompleted) {
                                                Text(
                                                    text = listElement.name,
                                                    modifier = Modifier.padding(start = 10.dp),
                                                    textDecoration = TextDecoration.LineThrough,
                                                    color = Color.Gray
                                                )
                                            } else {
                                                Text(
                                                    text = listElement.name,
                                                    modifier = Modifier.padding(start = 10.dp),
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }

                        1 -> Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
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
                                LazyColumn {
                                    items(viewModel.listTomorrow.value) { listElement ->
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                                .combinedClickable(onClick = {
                                                    CoroutineScope(Dispatchers.Default).launch {

                                                        viewModel.updateTask(
                                                            listElement.id, !listElement.isCompleted
                                                        )
                                                    }

                                                }, onLongClick = {
                                                    viewModel.deleteTask(listElement.id)
                                                })
                                        ) {
                                            if (listElement.isCompleted) {
                                                Text(
                                                    text = listElement.name,
                                                    modifier = Modifier.padding(start = 10.dp),
                                                    textDecoration = TextDecoration.LineThrough,
                                                    color = Color.Gray
                                                )
                                            } else {
                                                Text(
                                                    text = listElement.name,
                                                    modifier = Modifier.padding(start = 10.dp),
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                NewTaskButtonRow {
                    showSheet = true
                }
            }
        }

    })


}