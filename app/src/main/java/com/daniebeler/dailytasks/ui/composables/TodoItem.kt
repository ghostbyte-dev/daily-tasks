package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun TodoItem(
    task: Task,
    updateTask: (isCompleted: Boolean) -> Unit,
    updateText: (text: String) -> Unit,
    deleteTask: () -> Unit
) {

    val showBottomSheet = remember { mutableStateOf(false) }

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
                    showBottomSheet.value = true
                }
                .weight(1f)
                .padding(start = 12.dp, top = 12.dp, end = 0.dp, bottom = 12.dp)
                .fillMaxHeight()) {
            Text(text = task.name)

            if (task.isOverdue().first) {
                var text = stringResource(R.string.due_days_ago, task.isOverdue().second)
                if (task.isOverdue().second == 1L) {
                    text = stringResource(R.string.due_yesterday)
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
                    stringResource(R.string.due_today),
                    color = CUSTOM_GREEN,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = stringResource(R.string.planned_for_tomorrow),
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

    EditTaskBottomSheet(
        text = task.name,
        showState = showBottomSheet,
        updateTask = updateText,
        deleteTask = deleteTask
    )
}