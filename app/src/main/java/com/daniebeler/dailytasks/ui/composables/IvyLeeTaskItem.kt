import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

@Composable
fun IvyLeeTaskItem(
    index: Int,
    name: String,
    isPlaceholder: Boolean = false,
    onNameChange: (String) -> Unit = {},
    dragHandle: @Composable (() -> Unit)? = null
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 8.dp)
        ) {
            Box(Modifier.width(24.dp)) {
                Text(
                    text = "${index + 1}",
                    color = if (isPlaceholder)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.titleMedium
                )
            }


            Box(modifier = Modifier.weight(1f)) {
                // If it's a placeholder and empty, show the "Add task" hint
                if (isPlaceholder && name.isEmpty()) {
                    Text(
                        text = "Add task",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                BasicTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = if (isPlaceholder)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else
                            MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }

            if (!isPlaceholder) {
                dragHandle?.invoke()
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}