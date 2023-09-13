package com.softteco.template.ui.feature

import com.softteco.template.Constants

object ValidateFields {

    fun String.isEmailCorrect(): Boolean {
        return this.matches(Regex(Constants.EMAIL_PATTERN))
    }

    fun String.isHasMinimum(): Boolean {
        return this.matches(Regex(Constants.PASSWORD_PATTERN_MIN))
    }

    fun String.isHasCapitalizedLetter(): Boolean {
        return this.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))
    }
}

sealed class EmailFieldState {
    object Success : EmailFieldState()
    object Empty : EmailFieldState()
    object Waiting : EmailFieldState()
    object Error : EmailFieldState()
}

sealed class SimpleFieldState {
    object Success : SimpleFieldState()
    object Empty : SimpleFieldState()
    object Waiting : SimpleFieldState()
}

sealed class PasswordFieldState {
    object Success : PasswordFieldState()
    object Empty : PasswordFieldState()
    object Waiting : PasswordFieldState()
    object Error : PasswordFieldState()
}
