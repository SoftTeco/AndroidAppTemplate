package com.softteco.template.ui.components.snackBar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
class SnackbarController(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun showMessage(
        message: String,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = withDismissAction,
                duration = duration
            )
        }
    }
}
