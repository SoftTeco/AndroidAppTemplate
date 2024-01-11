package com.softteco.template.ui.components.snackBar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope

@Composable
fun ProvideSnackbarController(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSnackbarController provides SnackbarController(
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        ),
        content = content
    )
}

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("No SnackbarController provided.")
}
