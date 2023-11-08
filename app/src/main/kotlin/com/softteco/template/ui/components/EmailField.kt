package com.softteco.template.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.softteco.template.R
import com.softteco.template.ui.feature.EmailFieldState

@Composable
fun EmailField(
    emailValue: String,
    onEmailChanged: (String) -> Unit,
    fieldStateEmail: EmailFieldState,
    modifier: Modifier = Modifier,
) {
    Column {
        val isError = fieldStateEmail is EmailFieldState.Error
        OutlinedTextField(
            value = emailValue,
            onValueChange = { newValue ->
                onEmailChanged(newValue)
            },
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            isError = isError,
            supportingText = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = when (fieldStateEmail) {
                        is EmailFieldState.Empty -> stringResource(R.string.required)
                        is EmailFieldState.Error -> stringResource(R.string.email_not_valid)
                        else -> ""
                    },
                    color = if (isError) MaterialTheme.colorScheme.error else LocalContentColor.current
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
            ),
            singleLine = true,
        )
    }
}
