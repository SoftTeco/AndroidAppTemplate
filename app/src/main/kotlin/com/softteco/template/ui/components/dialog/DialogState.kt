package com.softteco.template.ui.components.dialog

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class DialogState(
    @StringRes val messageRes: Int,
    @StringRes val positiveBtnRes: Int,
    val positiveBtnAction: (() -> Unit)? = null,
    @StringRes val titleRes: Int? = null,
    @StringRes val negativeBtnRes: Int? = null,
    val negativeBtnAction: (() -> Unit)? = null,
    val dismissAction: (() -> Unit)? = null,
)
