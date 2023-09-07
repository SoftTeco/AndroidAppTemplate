package com.softteco.template.ui.feature

import androidx.annotation.StringRes
import com.softteco.template.Constants

object ValidateFields {

	fun String.isEmailCorrect(): Boolean {
		return this.matches(Regex(Constants.EMAIL_PATTERN))
	}

	fun String.isFieldEmpty(): Boolean {
		return this.isEmpty()
	}

//	fun validateEmail(fieldValue: String): FieldValidationState {
//		val isEmailCorrect = isEmailCorrect(fieldValue)
//
//		return FieldValidationState(
//			isEmailCorrect = isEmailCorrect
//		)
//	}

//	fun validatePassword(fieldValue: String): FieldValidationState {
//		val validateCapitalizedLetter = validateCapitalizedLetter(fieldValue)
//		val validateMinimum = validateMinimum(fieldValue)
//
//		return FieldValidationState(
//			hasMinimum = validateMinimum,
//			hasCapitalizedLetter = validateCapitalizedLetter
//		)
//	}

//	fun validateFieldEmpty(fieldValue: String): FieldValidationState {
//		val isEmpty = isEmpty(fieldValue)
//
//		return FieldValidationState(
//			isEmpty = isEmpty
//		)
//	}

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


//	fun isEmailCorrect(value: String): Boolean =
//		value.matches(Regex(Constants.EMAIL_PATTERN))
//}

//private fun validateCapitalizedLetter(password: String): Boolean =
//	password.matches(Regex(Constants.PASSWORD_PATTERN_CAPITALIZED_LETTER))
//
//fun validateMinimum(password: String): Boolean =
//	password.matches(Regex(Constants.PASSWORD_PATTERN_MIN))

//fun isEmpty(password: String): Boolean =
//	password.isEmpty()
}
data class FieldValidationState(
	var isEmailCorrect: Boolean = false,
	val hasMinimum: Boolean = false,
	val hasCapitalizedLetter: Boolean = false,
	var isPasswordCorrect: Boolean = hasMinimum && hasCapitalizedLetter,
	var isEmpty: Boolean = false,

	)

sealed class EmailFieldState {
	object Success : EmailFieldState()
	object Empty : EmailFieldState()
	class Waiting(@StringRes val labelRes: Int?) : EmailFieldState()
	class Error(@StringRes val labelRes: Int?) : EmailFieldState()
}

sealed class SimpleFieldState {
	object Success : SimpleFieldState()
	object Empty : SimpleFieldState()
	class Waiting(@StringRes val labelRes: Int?) : SimpleFieldState()
}

sealed class PasswordFieldState {
	object Success : PasswordFieldState()
	object Empty : PasswordFieldState()
	class Waiting(@StringRes val labelRes: Int) : PasswordFieldState()
	class Error(@StringRes val labelRes: Int) : PasswordFieldState()
}