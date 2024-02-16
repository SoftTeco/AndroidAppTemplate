package com.softteco.template.ui.components

import androidx.compose.runtime.Immutable

enum class FieldType {
    EMAIL,
    USERNAME,
    PASSWORD,
}

@Immutable
sealed interface TextFieldState {
    data object Empty : TextFieldState
    data object AwaitingInput : TextFieldState
    data object Valid : TextFieldState

    sealed interface Error : TextFieldState

    data class EmailError(val errorRes: Int) : Error
    data class UsernameError(val errorRes: Int) : Error
    data class PasswordError(val isRightLength: Boolean, val isUppercase: Boolean) : Error
}
