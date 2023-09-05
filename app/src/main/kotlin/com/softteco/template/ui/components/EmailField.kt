package com.softteco.template.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.softteco.template.R

@Composable
fun EmailField(
	strId: Int,
	value: String,
	textIdWarning: Int,
	colorWarning: Color,
	showWarning: Boolean,
	isEmailValid: Boolean,
	emailTextWarning: Int,

	modifier: Modifier = Modifier,
	onFieldValueChanged: ((String) -> Unit) = {}
) {
	OutlinedTextField(
		value = value,
		onValueChange = {
			onFieldValueChanged(it)
		},
		modifier = modifier,
		label = {
			Text(text = stringResource(id = strId))
		},
		isError = showWarning || isEmailValid
	)
	if (showWarning) {
		Text(text = stringResource(textIdWarning), color = colorWarning)
	}
	if (isEmailValid) {
		Text(text = stringResource(emailTextWarning), color = Color.Red)
	}
}

data class EmailFieldState(
	val textId: Int = R.string.required,
	val color: Color = Color.Red,
	val show: Boolean = false,
	val isEmailValid: Boolean = false,
	val emailNotValidTextId: Int = R.string.email_not_valid
)
