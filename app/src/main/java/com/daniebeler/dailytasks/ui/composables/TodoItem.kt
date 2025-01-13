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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniebeler.dailytasks.R
import com.daniebeler.dailytasks.db.Task
import com.daniebeler.dailytasks.db.isOverdue
import com.daniebeler.dailytasks.db.isUntilToday
import com.daniebeler.dailytasks.utils.CUSTOM_GREEN
import com.daniebeler.dailytasks.utils.CUSTOM_RED

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
        Column(
            Modifier
                .clickable {
                    showBottomSheet = true
                }
                .weight(1f)
                .padding(start = 12.dp, top = 12.dp, end = 0.dp, bottom = 12.dp)
                .fillMaxHeight()) {
            Text(text = task.name)

            if (task.isOverdue().first) {
                var text = "Due " + task.isOverdue().second + " days ago"
                if (task.isOverdue().second == 1L) {
                    text = "Due yesterday"
                }
                Text(
                    text,
                    color = CUSTOM_RED,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (task.isUntilToday()) {
                Text(
                    "Due today",
                    color = CUSTOM_GREEN,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    "Planned for tomorrow",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (task.isCompleted) {
            Box(
                Modifier
                    .clickable {
                        updateTask(false);
                    }
                    .padding(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(CUSTOM_GREEN)
                    .padding(4.dp)) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Check icon",
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
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(4.dp))
                    .padding(4.dp)) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Check icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
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
                    if (task.isCompleted) {
                        Button(
                            onClick = {
                                updateTask(!task.isCompleted)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(12.dp),
                        ) {
                            Text("Mark as done")
                        }
                    } else {
                        Button(
                            onClick = {
                                updateTask(!task.isCompleted)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(12.dp),
                        ) {
                            Text("Mark as undone")
                        }
                    }


                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            showBottomSheet = false
                            deleteTask()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CUSTOM_RED,
                            contentColor = Color.White
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