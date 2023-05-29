package com.softteco.template.presentation.common

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkColors = darkColorScheme(
    primary = Color(0xFF33303C),
    onPrimary = Color(0xFFFEFEFE),

    secondary = Color(0xFFCEAEF0),
    onSecondary = Color(0xFF000000),

    background = Color(0xFF2B2B38),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF1C1A27),
    onSurface = Color(0xFFFFFFFF),

    error = Color(0xFFFF5A5A),
    onError = Color(0xFFFFFFFF),
)

val LightColors = lightColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF33303C),

    secondary = Color(0xFFCEAEF0),
    onSecondary = Color(0xFF000000),

    background = Color(0xFFEEEEEE),
    onBackground = Color(0xFF000000),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),

    error = Color(0xFFFF5A5A),
    onError = Color(0xFFFFFFFF),
)
