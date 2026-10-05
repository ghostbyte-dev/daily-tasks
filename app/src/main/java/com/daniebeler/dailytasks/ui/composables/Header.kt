package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TodayHeader(modifier: Modifier = Modifier) {
    DayHeader(
        title = stringResource(R.string.today),
        date = LocalDate.now(),
        modifier = modifier
    )
}

@Composable
fun TomorrowHeader(modifier: Modifier = Modifier) {
    DayHeader(
        title = stringResource(R.string.tomorrow),
        date = LocalDate.now().plusDays(1),
        modifier = modifier
    )
}

@Composable
private fun DayHeader(
    title: String,
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    val locale = LocalLocale.current.platformLocale
    val formatter = remember(locale) {
        DateTimeFormatter.ofPattern("EEEE, MMMM d", locale)
    }

    Column(modifier = modifier.padding(bottom = 28.dp)) {
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
}