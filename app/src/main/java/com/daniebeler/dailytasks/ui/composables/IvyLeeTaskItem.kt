package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun IvyLeeTaskItem(
    index: Int,
    count: Int,
    name: String,
    onClick: () -> Unit = {},
    isCompleted: Boolean,
    today: Boolean,
    completeItem: () -> Unit
) {
    val completedColor = if (isSystemInDarkTheme()) Color(0xFF2E5E33) else Color(0xFFC8E6C9)
    val containerColor =
        if (isCompleted) completedColor else MaterialTheme.colorScheme.surfaceContainer


    SegmentedListItem(
        onClick = onClick,
        shapes = ListItemDefaults.segmentedShapes(
            index = index, count = count
        ),
        colors = ListItemDefaults.segmentedColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        content = {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
        },
        trailingContent = {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { completeItem() }
            )
        },
    )
}