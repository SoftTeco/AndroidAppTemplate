package com.softteco.template.ui.components.snackbar

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration

data class SnackbarState(
    @StringRes
    val messageRes: Int,
    @StringRes
    val actionLabelRes: Int? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
