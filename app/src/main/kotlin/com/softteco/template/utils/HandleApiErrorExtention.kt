package com.softteco.template.utils

import com.softteco.template.R
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.Result
import com.softteco.template.ui.components.SnackBarState
import kotlinx.coroutines.flow.MutableStateFlow

fun <T> handleApiError(
    result: Result<T>,
    errorStateFlow: MutableStateFlow<SnackBarState>
) {
    when (result) {
        is Result.Error -> {
            val textId = when (result.error) {
                is ErrorEntity.AccessDenied -> R.string.error_example
                is ErrorEntity.Network -> R.string.error_example
                is ErrorEntity.NotFound -> R.string.error_example
                is ErrorEntity.ServiceUnavailable -> R.string.error_example
                is ErrorEntity.Unknown -> R.string.error_example
            }
            errorStateFlow.value = SnackBarState(textId = textId, show = true)
        }

        else -> {
            // Handle other cases if needed
        }
    }
}
