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