package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.db.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(task: Task, updateTask: (isCompleted: Boolean) -> Unit, deleteTask: () -> Unit) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Row(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (task.isCompleted) {
            Box(
                Modifier
                    .clickable {
                        updateTask(false);
                    }
                    .padding(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp)) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Home icon",
                    tint = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Box(
                Modifier
                    .clickable {
                        updateTask(true);
                    }
                    .padding(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                    .padding(4.dp)) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Home icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            Modifier
                .clickable {
                    showBottomSheet = true
                }
                .weight(1f)
                .padding(12.dp)
                .fillMaxHeight()) {
            Text(text = task.name, modifier = Modifier
                .padding(start = 10.dp)
                .clickable {
                    showBottomSheet = true
                })
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false;
            }, sheetState = sheetState
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(task.name)

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            showBottomSheet = false
                            deleteTask()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(12.dp),
                    ) {
                        Text("Delete task")
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            showBottomSheet = false
                            deleteTask()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(12.dp),
                    ) {
                        Text("Delete task")
                    }
                }


            }
        }
    }
}