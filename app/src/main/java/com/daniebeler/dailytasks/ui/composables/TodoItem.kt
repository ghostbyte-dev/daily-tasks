package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
fun TodoItem(
    task: Task,
    updateTask: (isCompleted: Boolean) -> Unit,
    updateText: (text: String) -> Unit,
    deleteTask: () -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)



    var modalTextValue by remember {
        mutableStateOf(task.name)
    }

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

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false;
            }, sheetState = sheetState, containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            Column(Modifier.padding(24.dp)) {
                TextField(
                    value = modalTextValue,
                    onValueChange = { newText ->
                        modalTextValue = newText
                    },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        updateText(modalTextValue)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                )

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                    Button(
                        onClick = {
                            showBottomSheet = false
                            deleteTask()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CUSTOM_RED, contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 24.dp),
                    ) {
                        Text(stringResource(R.string.delete_task))
                    }
                }
            }
        }
    }
}