package com.softteco.template.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.softteco.template.R

@Composable
fun PasswordField(
	strId: Int,
	value: String,
	textIdWarning: Int,
	colorWarning: Color,
	showWarning: Boolean,
	modifier: Modifier = Modifier,
	onNameChanged: ((String) -> Unit) = {}
) {
	var passwordVisibility by remember { mutableStateOf(true) }

	OutlinedTextField(
		value = value,
		onValueChange = {
			onNameChanged(it)
		},
		modifier = modifier,
		label = {
			Text(text = stringResource(id = strId))
		},
		isError = showWarning,
		trailingIcon = {
			IconButton(onClick = {
				passwordVisibility = !passwordVisibility
			}) {
				Icon(
					imageVector = if (passwordVisibility) {
						Icons.Default.Create
					} else {
						Icons.Default.Done
					},
					contentDescription = stringResource(id = R.string.visibility),
					tint = Color.Black
				)
			}
		},
		visualTransformation = if (passwordVisibility) {
			PasswordVisualTransformation()
		} else {
			VisualTransformation.None
		}
	)
	if (showWarning) {
		Text(text = stringResource(id = textIdWarning), color = colorWarning)
	}
}

data class PasswordFieldState(
	val textId: Int = R.string.required,
	val color: Color = Color.Red,
	val show: Boolean = false,
)
