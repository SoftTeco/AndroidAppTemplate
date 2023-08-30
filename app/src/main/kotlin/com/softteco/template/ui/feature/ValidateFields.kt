package com.softteco.template.ui.feature

import com.softteco.template.Constants

class ValidateFields {
	fun execute(fieldValue: String): FieldValidationState {
		val isEmailCorrect = isEmailCorrect(fieldValue)

		return FieldValidationState(
			isEmailCorrect = isEmailCorrect
		)
	}
	private fun isEmailCorrect(value: String): Boolean =
		value.matches(Regex(Constants.EMAIL_PATTERN))
}

data class FieldValidationState(
	var isEmailCorrect: Boolean = false
)
