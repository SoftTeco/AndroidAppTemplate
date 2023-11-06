package com.softteco.template.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.softteco.template.R
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.theme.Dimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordField(
    passwordValue: String,
    onPasswordChanged: (String) -> Unit,
    fieldStatePassword: PasswordFieldState,
    modifier: Modifier = Modifier,
    isPasswordHasMinimum: Boolean? = null,
    isPasswordHasUpperCase: Boolean? = null,
) {
    Column {
        var passwordVisibility by remember { mutableStateOf(true) }
        val isError = fieldStatePassword is PasswordFieldState.Empty
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = passwordValue,
            onValueChange = { newValue ->
                onPasswordChanged(newValue)
            },
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = when (fieldStatePassword) {
                            is PasswordFieldState.Empty -> stringResource(R.string.required)
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        imageVector = if (passwordVisibility) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                        contentDescription = stringResource(id = R.string.visibility),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true,
        )
    }
    if (isPasswordHasMinimum != null && isPasswordHasUpperCase != null) {
        Spacer(modifier = Modifier.height(Dimens.PaddingDefault))
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingDefault)) {
            ConditionRow(
                condition = stringResource(R.string.registration_password_condition1),
                check = isPasswordHasMinimum
            )
            ConditionRow(
                condition = stringResource(R.string.registration_password_condition2),
                check = isPasswordHasUpperCase
            )
        }
    }
}

@Composable
private fun ConditionRow(
    condition: String,
    check: Boolean,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (check) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        label = "ColorAnimation"
    )

    val icon = if (check) {
        Icons.Rounded.Check
    } else {
        Icons.Rounded.Close
    }

    Row(modifier = modifier) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = stringResource(id = R.string.visibility)
        )
        Spacer(modifier = Modifier.width(Dimens.PaddingDefault))
        Text(
            text = condition,
            color = color
        )
    }
}
