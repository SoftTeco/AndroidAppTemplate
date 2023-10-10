package com.softteco.template.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SecondaryButton(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        OutlinedButton(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() }
        ) {
            Text(title)
        }
    }
}
