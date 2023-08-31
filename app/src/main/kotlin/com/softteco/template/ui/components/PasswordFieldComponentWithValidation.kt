package com.softteco.template.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.softteco.template.R
import com.softteco.template.ui.feature.FieldValidationState
import com.softteco.template.ui.theme.Dimens

@Composable
fun PasswordFieldComponentWithValidation(
    value: String,
    fieldErrorState: Boolean,
    passwordError: FieldValidationState,
    modifier: Modifier = Modifier,
    onFieldValueChanged: ((String) -> Unit) = {}
) {
    val context = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(true) }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onFieldValueChanged(it)
            },
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            isError = fieldErrorState,
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
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
        )
        if (fieldErrorState) {
            Text(text = stringResource(id = R.string.required), color = Color.Red)
        }
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

        Column(verticalArrangement = Arrangement.spacedBy(Dimens.Padding5)) {
            ConditionRow(
                condition = context.getString(R.string.registration_password_condition1),
                check = passwordError.hasMinimum
            )
            ConditionRow(
                condition = context.getString(R.string.registration_password_condition2),
                check = passwordError.hasCapitalizedLetter
            )
        }
    }
}

@Composable
fun ConditionRow(
    condition: String,
    check: Boolean,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (check) Color.Green else Color.Red
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
        Spacer(modifier = Modifier.width(Dimens.Padding10))
        Text(
            text = condition,
            color = color
        )
    }
}
