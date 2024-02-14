package com.softteco.template.ui.components.snackbar

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SnackbarController @Inject constructor(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
    @ApplicationContext
    private val context: Context,
) {
    private val _onDismissEvents = MutableSharedFlow<Unit>()
    val onDismissEvents get() = _onDismissEvents.asSharedFlow()

    fun showSnackbar(@StringRes messageRes: Int) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(context.getString(messageRes))
        }
    }

    fun showSnackbar(snackbarState: SnackBarState) {
        coroutineScope.launch {
            snackbarState.run {
                snackbarHostState.showSnackbar(
                    message = context.getString(messageRes),
                    actionLabel = actionLabelRes?.let { context.getString(it) },
                    withDismissAction = withDismissAction,
                    duration = duration
                )
                _onDismissEvents.emit(Unit)
            }
        }
    }
}

