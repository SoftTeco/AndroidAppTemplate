package com.softteco.template.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
        val isError =
            fieldStateEmail is EmailFieldState.Empty || fieldStateEmail is EmailFieldState.Error
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
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = when (fieldStateEmail) {
                            is EmailFieldState.Empty -> stringResource(R.string.required)
                            is EmailFieldState.Error -> stringResource(R.string.email_not_valid)
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
    }
}
