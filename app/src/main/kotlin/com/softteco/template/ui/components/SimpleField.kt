package com.softteco.template.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.softteco.template.R

@Composable
fun SimpleField(
	modifier: Modifier = Modifier,
	strId: Int,
	value: String,
	fieldErrorState: Boolean,
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
	if (fieldErrorState) {
		Text(text = stringResource(id = R.string.required), color = Color.Red)
	}
}
