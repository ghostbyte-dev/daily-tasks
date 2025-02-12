package com.daniebeler.dailytasks.ui.composables

import android.view.Gravity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.daniebeler.dailytasks.R
import com.daniebeler.dailytasks.utils.CUSTOM_GREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskBottomSheet(
    showState: MutableState<Boolean>,
    isForToday: Boolean,
    storeTask: (text: String) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    var modalTextValue by remember {
        mutableStateOf("")
    }

    if (showState.value) {
        val transitionState = remember {
            MutableTransitionState(false).apply { targetState = true }
        }

        Dialog(
            onDismissRequest = { showState.value = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

            AnimatedVisibility(visibleState = transitionState,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Surface(
                    shape = BottomSheetDefaults.ExpandedShape,
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Column(Modifier.padding(16.dp)) {
                        if (isForToday) {
                            Text(
                                modifier = Modifier.padding(start = 14.dp),
                                text = stringResource(R.string.new_task_for_today),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                modifier = Modifier.padding(start = 14.dp),
                                text = stringResource(R.string.new_task_for_tomorrow),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TextField(
                                value = modalTextValue,
                                onValueChange = { newText ->
                                    modalTextValue = newText
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    capitalization = KeyboardCapitalization.Sentences,
                                    keyboardType = KeyboardType.Text
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    if (modalTextValue.isNotBlank()) {
                                        storeTask(modalTextValue)
                                        keyboardController?.hide()
                                        modalTextValue = ""
                                        focusManager.clearFocus()
                                    }
                                }),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colorScheme.onSurface,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
                            )

                            Button(
                                onClick = {
                                    if (modalTextValue.isNotBlank()) {
                                        storeTask(modalTextValue)
                                        keyboardController?.hide()
                                        modalTextValue = ""
                                        focusManager.clearFocus()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 24.dp, vertical = 12.dp
                                ),
                            ) {
                                Text(text = stringResource(R.string.save))
                            }
                        }

                        LaunchedEffect(key1 = Unit) {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                    }
                }
            }
        }
    }
}