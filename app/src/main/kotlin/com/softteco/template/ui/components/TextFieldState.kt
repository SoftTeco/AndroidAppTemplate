package com.softteco.template.ui.components

import androidx.compose.runtime.Immutable

enum class FieldType {
    EMAIL,
    USERNAME,
    PASSWORD,
}

@Immutable
data class TextFieldState(
    val text: String = "",
    val state: FieldState = FieldState.Empty,
)

@Immutable
sealed interface FieldState {
    data object Empty : FieldState
    data object AwaitingInput : FieldState
    data object Valid : FieldState

    sealed interface Error : FieldState

    data class EmailError(val errorRes: Int) : Error
    data class UsernameError(val errorRes: Int) : Error
    data class PasswordError(val isRightLength: Boolean, val isUppercase: Boolean) : Error
}
