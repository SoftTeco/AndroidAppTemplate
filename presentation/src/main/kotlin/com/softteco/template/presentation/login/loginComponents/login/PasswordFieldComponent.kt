package com.softteco.template.presentation.login.loginComponents.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.softteco.template.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldComponent(
    fieldName: MutableState<TextFieldValue>,
    fieldNameErrorState: MutableState<Boolean>,
    fieldNameStr: Int,
    passwordVisibility: MutableState<Boolean>
) {

    OutlinedTextField(
        value = fieldName.value,
        onValueChange = {
            if (fieldNameErrorState.value) {
                fieldNameErrorState.value = false
            }
            fieldName.value = it
        },
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.password))
        },
        isError = fieldNameErrorState.value,
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(
                    imageVector = if (passwordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "visibility",
                    tint = Color.Black
                )
            }
        },
        visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
    )
    if (fieldNameErrorState.value) {
        Text(text = stringResource(id = R.string.required), color = Color.Red)
    }
}