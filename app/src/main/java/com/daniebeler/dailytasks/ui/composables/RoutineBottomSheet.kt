package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBottomSheet(
    initialName: String = "",
    initialInterval: Int = 1,
    onDismiss: () -> Unit,
    onSave: (name: String, intervalDays: Int) -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var interval by rememberSaveable { mutableIntStateOf(initialInterval) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                if (onDelete == null) "New routine" else "Edit routine",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Routine") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = interval == 1,
                    onClick = { interval = 1 },
                    label = { Text("Daily") })
                FilterChip(
                    selected = interval == 7,
                    onClick = { interval = 7 },
                    label = { Text("Weekly") })
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    if (interval == 1) "Every day" else "Every $interval days",
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { interval = (interval - 1).coerceAtLeast(1) },
                        enabled = interval > 1
                    ) { Icon(Icons.Rounded.Remove, "Decrease") }
                    IconButton(
                        onClick = {
                            interval = (interval + 1).coerceAtMost(365)
                        }) { Icon(Icons.Rounded.Add, "Increase") }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onDelete != null) {
                    OutlinedButton(
                        onClick = {
                            onDelete()
                            onDismiss()
                        }, colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ), modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Text(
                            "Delete",
                            modifier = Modifier.padding(start = ButtonDefaults.IconSpacing)
                        )
                    }
                }
                Button(
                    onClick = {
                        onSave(name.trim(), interval)
                        onDismiss()
                    }, enabled = name.isNotBlank(), modifier = Modifier.weight(1f)
                ) { Text("Save") }
            }
        }
    }
}