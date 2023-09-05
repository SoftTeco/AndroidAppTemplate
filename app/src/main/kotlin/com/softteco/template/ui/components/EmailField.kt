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
	fieldErrorState: Boolean,
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
		isError = fieldErrorState
	)
	if (showWarning) {
		Text(text = stringResource(textIdWarning), color = colorWarning)
	}
}

data class EmailFieldState(
	val textId: Int = R.string.required,
	val color: Color = Color.Red,
	val show: Boolean = false,
)
