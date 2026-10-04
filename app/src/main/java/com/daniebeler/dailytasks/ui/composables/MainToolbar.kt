package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

private data class ToolbarDestination(val label: String, val icon: ImageVector)

@Composable
fun MainToolbar(
    currentPage: () -> Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val destinations = listOf(
        ToolbarDestination("Today", Icons.Rounded.Today),
        ToolbarDestination("Tomorrow", Icons.Rounded.Event)
    )

    HorizontalFloatingToolbar(expanded = true, modifier = modifier) {
        destinations.forEachIndexed { index, destination ->
            if (currentPage() == index) {
                Button(onClick = { onSelect(index) }) {
                    Icon(destination.icon, null, Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(destination.label)
                }
            } else {
                TextButton(onClick = { onSelect(index) }) { Text(destination.label) }
            }
        }
    }
}