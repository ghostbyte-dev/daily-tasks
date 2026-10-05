package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TodayHeader(onNavigate: () -> Unit, modifier: Modifier = Modifier) {
    DayHeader(
        title = stringResource(R.string.today),
        date = LocalDate.now(),
        navLabel = "Plan tomorrow",
        navForward = true,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun TomorrowHeader(onNavigate: () -> Unit, modifier: Modifier = Modifier) {
    DayHeader(
        title = stringResource(R.string.tomorrow),
        date = LocalDate.now().plusDays(1),
        navLabel = "Today",
        navForward = false,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
private fun DayHeader(
    title: String,
    date: LocalDate,
    navLabel: String,
    navForward: Boolean,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = LocalLocale.current.platformLocale
    val formatter = remember(locale) {
        DateTimeFormatter.ofPattern("EEEE, MMMM d", locale)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = date.format(formatter),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TextButton(onClick = onNavigate) {
            if (!navForward) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            Text(navLabel, style = MaterialTheme.typography.labelLarge)
            if (navForward) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }
    }
}