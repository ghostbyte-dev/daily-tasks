package com.daniebeler.dailytasks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.ui.theme.DailyTasksTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    lateinit var dbHandler: DBHandler

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHandler = DBHandler(this)

        setContent {

            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()

            var modalTextValue by remember {
                mutableStateOf("")
            }

            var listToday by remember {
                mutableStateOf(mutableListOf<ToDoItem>())
            }

            var listTomorrow by remember {
                mutableStateOf(mutableListOf<ToDoItem>())
            }

            val focusRequester = remember { FocusRequester() }

            listToday = dbHandler.getToDos("today")
            listTomorrow = dbHandler.getToDos("tomorrow")

            var tabIndex by remember { mutableStateOf(0) }
            val pagerState = rememberPagerState()

            var checked by remember { mutableStateOf(false) }


            DailyTasksTheme {
                if (sheetState.isVisible) {

                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = {
                            scope.launch {
                                sheetState.hide()
                            }
                        },
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceAround) {
                            TextField(
                                value = modalTextValue, onValueChange = { newText ->
                                    modalTextValue = newText
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester)
                            )


                            Button(onClick = {
                                if (modalTextValue.isNotBlank()) {
                                    val toDo = ToDoItem()
                                    toDo.name = modalTextValue

                                    if (tabIndex == 0) {
                                        toDo.date = LocalDate.now()
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    } else {
                                        toDo.date = LocalDate.now().plusDays(1)
                                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    }

                                    dbHandler.addToDo(toDo)

                                    listToday = dbHandler.getToDos("today")
                                    listTomorrow = dbHandler.getToDos("tomorrow")
                                    scope.launch {
                                        sheetState.hide()
                                    }
                                }
                            }) {
                                Text(text = "save")
                            }
                        }
                    }

                    LaunchedEffect(key1 = Unit) {
                        focusRequester.requestFocus()
                    }
                }

                Column(
                    Modifier.fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        FilledIconToggleButton(
                            checked = checked,
                            onCheckedChange = { checked = it }) {
                            if (checked) {
                                Icon(
                                    Icons.Filled.Lock,
                                    contentDescription = "Localized description"
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.Lock,
                                    contentDescription = "Localized description"
                                )
                            }
                        }

                        Button(onClick = {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://jamesclear.com/ivy-lee")
                                )
                            )
                        }) {
                            Text(text = "The Ivy Lee Method")
                        }
                    }


                    TabRow(selectedTabIndex = tabIndex) {
                        Tab(text = { Text("Today") }, selected = tabIndex == 0, onClick = {
                            tabIndex = 0
                            scope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(0)
                            }

                        })

                        Tab(text = { Text("Tomorrow") }, selected = tabIndex == 0, onClick = {
                            tabIndex = 1
                            scope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(1)
                            }
                        })
                    }

                    HorizontalPager(
                        state = pagerState,
                        pageCount = 2,
                        modifier = Modifier.weight(1f)
                    ) { tabIndex ->
                        when (tabIndex) {
                            0 ->
                                LazyColumn(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    itemsIndexed(listToday) { index, listElement ->
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                                .combinedClickable(
                                                    onClick = {
                                                        dbHandler.updateToDo(index, "today")
                                                        listToday = dbHandler.getToDos("today")
                                                    },
                                                    onLongClick = {
                                                        dbHandler.deleteToDo(index, "today")
                                                        listToday = dbHandler.getToDos("today")
                                                    }
                                                )
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
                                                    modifier = Modifier.padding(start = 10.dp)
                                                )
                                            }

                                        }
                                    }
                                }

                            1 ->
                                LazyColumn(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    itemsIndexed(listTomorrow) { index, listElement ->
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                                .combinedClickable(
                                                    onClick = {
                                                        dbHandler.updateToDo(index, "tomorrow")
                                                        listTomorrow =
                                                            dbHandler.getToDos("tomorrow")
                                                    },
                                                    onLongClick = {
                                                        dbHandler.deleteToDo(index, "tomorrow")
                                                        listTomorrow =
                                                            dbHandler.getToDos("tomorrow")
                                                    }
                                                )
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
                                                    modifier = Modifier.padding(start = 10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(onClick = {
                            scope.launch {
                                sheetState.show()
                            }
                        }) {
                            Text("New task")
                        }
                    }
                }
            }
        }
    }
}