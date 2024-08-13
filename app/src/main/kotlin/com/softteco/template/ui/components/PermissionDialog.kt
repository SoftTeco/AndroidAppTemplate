package com.softteco.template.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme

@Composable
fun PermissionDialog(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    modifier: Modifier = Modifier,
    onRequestPermission: () -> Unit = {}
) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = modifier.fillMaxWidth(),
            title = { Text(text = stringResource(id = titleRes)) },
            text = { Text(text = stringResource(id = descriptionRes)) },
            confirmButton = {
                Button(
                    onClick = {
                        onRequestPermission()
                        showWarningDialog = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(

                        contentColor = Color.White
                    )
                ) { Text(text = stringResource(id = R.string.request_permission)) }
            },
            onDismissRequest = { }
        )
    }
}

@Composable
fun RationaleDialog(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    modifier: Modifier = Modifier,
) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = modifier.fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(id = titleRes)
                )
            },
            text = { Text(text = stringResource(id = descriptionRes)) },
            confirmButton = {
                Button(
                    onClick = { showWarningDialog = false },
                    modifier = modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    )
                ) { Text(text = stringResource(id = R.string.ok)) }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        PermissionDialog(
            titleRes = R.string.location_permission_title,
            descriptionRes = R.string.location_permission_description
        ) {
        }
    }
}
