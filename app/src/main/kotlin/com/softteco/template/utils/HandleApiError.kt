package com.softteco.template.utils

import com.softteco.template.R
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.Result
import com.softteco.template.ui.components.SnackBarState
import kotlinx.coroutines.flow.MutableStateFlow

fun <T> handleApiError(
    result: Result.Error<T>,
    errorStateFlow: MutableStateFlow<SnackBarState>
) {
    val textId = when (result.error) {
        is ErrorEntity.AccessDenied -> R.string.error_access_denied
        is ErrorEntity.Network -> R.string.error_network
        is ErrorEntity.NotFound -> R.string.error_not_found
        is ErrorEntity.ServiceUnavailable -> R.string.error_service_unavailable
        is ErrorEntity.Unknown -> R.string.error_unknown
    }
    errorStateFlow.value = SnackBarState(textId = textId, show = result.error.isDisplayable)
}
