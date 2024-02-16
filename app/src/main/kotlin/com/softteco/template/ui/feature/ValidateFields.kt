package com.softteco.template.ui.feature

import com.softteco.template.Constants
import com.softteco.template.R
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState

fun String.validateInputValue(fieldType: FieldType): TextFieldState {
    return if (isEmpty()) {
        TextFieldState.Empty
    } else {
        when (fieldType) {
            FieldType.EMAIL -> validateEmail()
            FieldType.USERNAME -> validateUsername()
            FieldType.PASSWORD -> validatePassword()
        }
    }
}

private fun String.validateEmail(): TextFieldState {
    val regex = Constants.EMAIL_PATTERN.toRegex()
    return if (regex.matches(this)) {
        TextFieldState.Valid
    } else {
        TextFieldState.EmailError(R.string.email_not_valid)
    }
}

private fun String.validateUsername(): TextFieldState {
    val regex = Constants.USERNAME_PATTERN.toRegex()
    return if (regex.matches(this)) {
        TextFieldState.Valid
    } else {
        TextFieldState.UsernameError(R.string.username_not_valid)
    }
}

private fun String.validatePassword(): TextFieldState {
    return when {
        isEmpty() -> TextFieldState.Empty
        !isHasMinimum() || !isHasCapitalizedLetter() -> {
            TextFieldState.PasswordError(
                isRightLength = isHasMinimum(),
                isUppercase = isHasCapitalizedLetter()
            )
        }

        else -> TextFieldState.Valid
    }
}

private fun String.isHasMinimum(): Boolean {
    return this.matches(Regex(Constants.PASSWORD_PATTERN_MIN))
}

private fun String.isHasCapitalizedLetter(): Boolean {
    return this.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))
}
