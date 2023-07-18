package com.daniebeler.dailytasks

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.ui.theme.DailyTasksTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    lateinit var dbHandler: DBHandler

    lateinit var themeSharedPreferences: SharedPreferences

    @OptIn(ExperimentalMaterial3Api::class)
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

            listToday = dbHandler.getToDos("today")

            var tabIndex by remember { mutableStateOf(0) }

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
                            TextField(value = modalTextValue, onValueChange = { newText ->
                                modalTextValue = newText
                            },
                                modifier = Modifier.weight(1f))

                            Button(onClick = {
                                if (modalTextValue.isNotBlank()) {
                                    val toDo = ToDoItem()
                                    toDo.name = modalTextValue

                                    if(tabIndex == 0){
                                        toDo.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    }
                                    else{
                                        toDo.date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    }

                                    dbHandler.addToDo(toDo)

                                    listToday = dbHandler.getToDos("today")
                                    scope.launch {
                                        sheetState.hide()
                                    }
                                }
                            }) {
                                Text(text = "save")
                            }
                        }
                    }
                }

                Column {
                    Row {
                        Text(text = "Header")
                    }


                    TabRow(selectedTabIndex = 0) {
                        Tab(text = { Text("Today") }, selected = tabIndex == 0,  onClick = { /*TODO*/ })

                        Tab(text = { Text("Tomorrow") }, selected = tabIndex == 0, onClick = { /*TODO*/ })
                    }

                    LazyColumn {
                        itemsIndexed(listToday) { index, listElement ->
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = listElement.name, modifier = Modifier.padding(start = 10.dp))
                            }
                        }
                    }



                    Button(onClick = {
                        scope.launch {
                            sheetState.show()
                        }
                    }) {
                        Text("Show sheet")
                    }

                }
            }




        }


        }
    }