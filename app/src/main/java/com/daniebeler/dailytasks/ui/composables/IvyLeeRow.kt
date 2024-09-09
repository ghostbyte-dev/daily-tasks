package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daniebeler.dailytasks.R

@Composable
fun IvyLeeRow() {

    val uriHandler = LocalUriHandler.current

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Button(onClick = {
            uriHandler.openUri("https://jamesclear.com/ivy-lee")
        }) {
            Text(text = stringResource(R.string.the_ivy_lee_method))
        }
    }
}