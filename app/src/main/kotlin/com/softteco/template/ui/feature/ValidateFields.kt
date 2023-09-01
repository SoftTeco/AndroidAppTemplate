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

	fun validateFieldEmpty(fieldValue: String): FieldValidationState {
		val isEmpty = isEmpty(fieldValue)

		return FieldValidationState(
			isEmpty = isEmpty
		)
	}

//	fun isAllLoginFieldsValid(): FieldValidationState {
//		var isFieldsValid = false
//		if (!FieldValidationState().isEmpty && FieldValidationState().isEmailCorrect) {
//			isFieldsValid = true
//		}
////	val isFieldsValid=  !FieldValidationState().isEmpty&&FieldValidationState().isEmailCorrect
//		return FieldValidationState(
//			isAllLoginFieldsValid = isFieldsValid
//		)
//	}


	fun isEmailCorrect(value: String): Boolean =
		value.matches(Regex(Constants.EMAIL_PATTERN))
}

private fun validateCapitalizedLetter(password: String): Boolean =
	password.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))

fun validateMinimum(password: String): Boolean =
	password.matches(Regex(Constants.PASSWORD_PATTERN_MIN))

fun isEmpty(password: String): Boolean =
	password.isEmpty()

data class FieldValidationState(
	var isEmailCorrect: Boolean = false,
	val hasMinimum: Boolean = false,
	val hasCapitalizedLetter: Boolean = false,
	var isPasswordCorrect: Boolean = hasMinimum && hasCapitalizedLetter,
	var isEmpty: Boolean = false,

)
