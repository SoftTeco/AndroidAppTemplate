package com.softteco.template.ui.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme

@Composable
fun AppDialog(state: DialogState, onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = {
            state.dismissAction?.invoke()
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                state.positiveBtnAction?.invoke()
                onDismissRequest()
            }) {
                Text(stringResource(state.positiveBtnRes))
            }
        },
        dismissButton = {
            state.negativeBtnRes?.let {
                TextButton(onClick = {
                    state.negativeBtnAction?.invoke()
                    onDismissRequest()
                }) {
                    Text(stringResource(it))
                }
            }
        },
        title = { state.titleRes?.let { Text(stringResource(it)) } },
        text = { Text(stringResource(state.messageRes)) },
        modifier = modifier,
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AppDialog(
                state = DialogState(
                    messageRes = R.string.error_email_not_found,
                    positiveBtnRes = R.string.ok,
                ),
                onDismissRequest = {},
            )
        }
    }
}
