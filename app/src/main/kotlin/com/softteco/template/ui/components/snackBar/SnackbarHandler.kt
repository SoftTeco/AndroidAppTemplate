package com.softteco.template.ui.components.snackBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.softteco.template.ui.LocalSnackbarController

@Composable
fun SnackbarHandler(
    snackbarState: SnackBarState,
    onDismissSnackbar: () -> Unit,
    snackbarController: SnackbarController = LocalSnackbarController.current
) {
    if (!snackbarState.show) return
    val message = stringResource(id = snackbarState.textId)
    LaunchedEffect(snackbarState) {
        snackbarController.showMessage(
            message = message
        )

        onDismissSnackbar()
    }
}

data class SnackBarState(
    val textId: Int = android.R.string.unknownName,
    val show: Boolean = false,
)
