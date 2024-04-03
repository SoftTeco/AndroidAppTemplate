package com.softteco.template.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme

@Composable
internal fun EditTextDialog(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onDismiss: (String?) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int? = null,
    @StringRes labelRes: Int? = null,
) {
    AlertDialog(
        onDismissRequest = { onDismiss(null) },
        title = { titleRes?.let { Text(stringResource(it)) } },
        text = {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { labelRes?.let { Text(stringResource(labelRes)) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                ),
                singleLine = true,
            )
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(null) }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss(value.text) }) {
                Text(stringResource(R.string.ok))
            }
        },
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EditTextDialog(
                value = TextFieldValue(""),
                onValueChange = {},
                onDismiss = {},
                titleRes = R.string.edit_name_dialog_title,
                labelRes = R.string.full_name_title
            )
        }
    }
}
