package com.softteco.template.ui.components.dialog

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogController @Inject constructor() {

    private val _dialogs = MutableStateFlow<Set<DialogState>>(emptySet())
    val dialogs get() = _dialogs.asStateFlow()

    private val _onDismissEvents = MutableSharedFlow<DialogState>()
    val onDismissEvents get() = _onDismissEvents.asSharedFlow()

    fun showDialog(dialogState: DialogState) {
        _dialogs.update { it.plus(dialogState) }
    }

    fun dismissDialog() {
        if (_dialogs.value.isNotEmpty()) {
            _dialogs.update { dialogs ->
                val visibleDialog = dialogs.first()
                _onDismissEvents.tryEmit(visibleDialog)
                dialogs.minus(visibleDialog)
            }
        }
    }
}
