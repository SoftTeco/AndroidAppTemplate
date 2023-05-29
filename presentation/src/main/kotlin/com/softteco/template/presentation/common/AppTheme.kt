package com.softteco.template.presentation.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun AppTheme(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val materialColors = if (isDarkMode) DarkColors else LightColors
    MaterialTheme(colorScheme = materialColors, shapes = Shapes) {
        Surface(modifier = modifier) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
                content = content
            )
        }
    }
}
