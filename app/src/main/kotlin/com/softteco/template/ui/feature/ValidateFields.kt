package com.softteco.template.ui.feature

import com.softteco.template.Constants
import com.softteco.template.R
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType

fun String.validateInputValue(fieldType: FieldType): FieldState {
    return if (isEmpty()) {
        FieldState.Empty
    } else {
        when (fieldType) {
            FieldType.EMAIL -> validateEmail()
            FieldType.USERNAME -> validateUsername()
            FieldType.PASSWORD -> validatePassword()
        }
    }
}

private fun String.validateEmail(): FieldState {
    val regex = Constants.EMAIL_PATTERN.toRegex()
    return if (regex.matches(this)) {
        FieldState.Valid
    } else {
        FieldState.EmailError(R.string.email_not_valid)
    }
}

private fun String.validateUsername(): FieldState {
    val regex = Constants.USERNAME_PATTERN.toRegex()
    return if (regex.matches(this)) {
        FieldState.Valid
    } else {
        FieldState.UsernameError(R.string.username_not_valid)
    }
}

private fun String.validatePassword(): FieldState {
    return when {
        isEmpty() -> FieldState.Empty
        !isHasMinimum() || !isHasCapitalizedLetter() -> {
            FieldState.PasswordError(
                isRightLength = isHasMinimum(),
                isUppercase = isHasCapitalizedLetter()
            )
        }

        else -> FieldState.Valid
    }
}

private fun String.isHasMinimum(): Boolean {
    return this.matches(Regex(Constants.PASSWORD_PATTERN_MIN))
}

private fun String.isHasCapitalizedLetter(): Boolean {
    return this.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))
}
