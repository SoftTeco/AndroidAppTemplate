package com.softteco.template.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
internal fun EditTextDialog(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onDismiss: (String?) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int? = null,
    @StringRes labelRes: Int? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Dialog(onDismissRequest = { onDismiss(null) }) {
        ElevatedCard(
            modifier,
            shape = ShapeDefaults.Small,
        ) {
            Column(
                Modifier.padding(
                    horizontal = Dimens.PaddingDefault,
                    vertical = Dimens.PaddingNormal
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingDefault)
            ) {
                titleRes?.let { Text(stringResource(it), style = titleStyle) }

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

                Row {
                    SecondaryButton(
                        title = stringResource(android.R.string.cancel),
                        loading = false,
                        modifier = Modifier.weight(1f),
                        onClick = { onDismiss(null) }
                    )
                    Spacer(modifier = Modifier.width(Dimens.PaddingDefault))
                    PrimaryButton(
                        buttonText = stringResource(android.R.string.ok),
                        modifier = Modifier.weight(1f),
                        loading = false,
                    ) {
                        onDismiss(value.text)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        EditTextDialog(
            value = TextFieldValue(""),
            onValueChange = {},
            onDismiss = {},
            titleRes = R.string.edit_name_dialog_title,
            labelRes = R.string.full_name_title
        )
    }
}
