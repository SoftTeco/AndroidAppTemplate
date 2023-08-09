package com.softteco.template.presentation.login.loginComponents.registration

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.PasValidationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldComponentWithValidation(
    viewModel: PasValidationViewModel,
    fieldNameErrorState: MutableState<Boolean>,
    passwordVisibility: MutableState<Boolean>
) {
    val passwordError by viewModel.passwordError.collectAsState()
    val context = LocalContext.current

    OutlinedTextField(
        value = viewModel.password,
        onValueChange = {
            if (fieldNameErrorState.value) {
                fieldNameErrorState.value = false
            }
            viewModel.changePassword(it)
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
    Spacer(modifier = Modifier.height(8.dp))

    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        ConditionRow(
            condition = context.getString(R.string.registration_password_condition1),
            check = passwordError.hasMinimum
        )
        ConditionRow(
            condition = context.getString(R.string.registration_password_condition1),
            check = passwordError.hasCapitalizedLetter
        )
    }
}

@Composable
fun ConditionRow(
    condition: String, check: Boolean
) {
    val color by animateColorAsState(
        targetValue = if (check) Color.Green else Color.Red, label = "text color"
    )

    val icon = if (check) {
        Icons.Rounded.Check
    } else {
        Icons.Rounded.Close
    }

    Row {
        Icon(
            imageVector = icon, tint = color, contentDescription = "status icon"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = condition, color = color
        )
    }
}