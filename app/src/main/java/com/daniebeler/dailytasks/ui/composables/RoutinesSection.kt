package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.db.Routine
import com.daniebeler.dailytasks.db.intervalLabel

@Composable
fun RoutinesSection(viewModel: MainScreenViewModel, modifier: Modifier = Modifier) {
    val routines by viewModel.routines
    var editing by remember { mutableStateOf<Routine?>(null) }
    var showNew by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            "Routines",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )

        routines.forEach { routine ->
            ListItem(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { editing = routine },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                leadingContent = { Icon(Icons.Rounded.Repeat, contentDescription = null) },
                headlineContent = { Text(routine.name) },
                supportingContent = { Text(routine.intervalLabel()) }
            )
        }

        TextButton(onClick = { showNew = true }) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Text("New routine", modifier = Modifier.padding(start = ButtonDefaults.IconSpacing))
        }
    }

    if (showNew) {
        RoutineBottomSheet(
            onDismiss = { showNew = false },
            onSave = { name, interval -> viewModel.addRoutine(name, interval) }
        )
    }

    editing?.let { routine ->
        RoutineBottomSheet(
            initialName = routine.name,
            initialInterval = routine.intervalDays,
            onDismiss = { editing = null },
            onSave = { name, interval -> viewModel.updateRoutine(routine.id, name, interval) },
            onDelete = { viewModel.deleteRoutine(routine.id) }
        )
    }
}