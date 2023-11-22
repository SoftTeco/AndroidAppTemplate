package com.softteco.template.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
) {
    Column {
        var passwordVisibility by remember { mutableStateOf(true) }
        val isError = fieldStatePassword is PasswordFieldState.Error
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
            supportingText = { SupportingText(passwordState = fieldStatePassword) },
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
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            )
        )
    }
}

@Composable
private fun SupportingText(passwordState: PasswordFieldState, modifier: Modifier = Modifier) {
    Column(modifier.height(48.dp)) {
        when (passwordState) {
            PasswordFieldState.Empty -> Text(stringResource(R.string.required))
            is PasswordFieldState.Error -> {
                Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraSmall)) {
                    ConditionRow(
                        condition = stringResource(R.string.registration_password_condition1),
                        check = passwordState.isRightLength
                    )
                    ConditionRow(
                        condition = stringResource(R.string.registration_password_condition2),
                        check = passwordState.isUppercase
                    )
                }
            }

            PasswordFieldState.Success -> { /* NOOP */
            }
        }
    }
}

@Composable
private fun ConditionRow(
    condition: String,
    check: Boolean,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        targetValue = if (check) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error,
        label = "ColorAnimation"
    )

    val icon = if (check) Icons.Rounded.Check else Icons.Rounded.Close

    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = R.string.visibility),
            Modifier.size(18.dp),
            tint = color,
        )
        Text(
            text = condition,
            color = color
        )
    }
}
