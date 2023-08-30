package com.softteco.template.ui.feature

import javax.annotation.concurrent.Immutable

class ValidateFields {
	fun execute(fieldValue: String): FieldValidationState {
		val isEmpty = isEmpty(fieldValue)
		val hasError = listOf(
			isEmpty
		).all { it }

		return FieldValidationState(
			successful = hasError,
			isEmpty = isEmpty
		)
	}

	private fun isEmpty(value: String): Boolean =
		value.isEmpty()
}

@Immutable
data class FieldValidationState(
	var successful: Boolean = false,
	var isEmpty: Boolean = false
)
