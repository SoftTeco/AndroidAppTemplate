package com.softteco.template.ui.feature

import com.softteco.template.Constants

class ValidateFields {
    fun validateEmail(fieldValue: String): FieldValidationState {
        val isEmailCorrect = isEmailCorrect(fieldValue)

        return FieldValidationState(
            isEmailCorrect = isEmailCorrect
        )
    }

    fun validatePassword(fieldValue: String): FieldValidationState {
        val validateCapitalizedLetter = validateCapitalizedLetter(fieldValue)
        val validateMinimum = validateMinimum(fieldValue)

        return FieldValidationState(
            hasMinimum = validateMinimum,
            hasCapitalizedLetter = validateCapitalizedLetter
        )
    }

    private fun isEmailCorrect(value: String): Boolean =
        value.matches(Regex(Constants.EMAIL_PATTERN))
}

private fun validateCapitalizedLetter(password: String): Boolean =
    password.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))

private fun validateMinimum(password: String): Boolean =
    password.matches(Regex(Constants.PASSWORD_PATTERN_MIN))

data class FieldValidationState(
    var isEmailCorrect: Boolean = false,
    val hasMinimum: Boolean = false,
    val hasCapitalizedLetter: Boolean = false,

)
