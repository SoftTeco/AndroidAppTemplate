package com.softteco.template.presentation.login.loginComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softteco.template.presentation.R

@Composable
fun CustomAlertDialog(onDismiss: () -> Unit, onExit: () -> Unit, message: String) {

    AlertDialog(
        onDismissRequest = {
            onDismiss
        },
        confirmButton = {
            TextButton(onClick = {
                onExit
            }) {
                Text(text = stringResource(id = R.string.ok))
            }
        },

        text = {
            Text(text = message)

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        shape = RoundedCornerShape(5.dp),
        containerColor = Color.White
    )
}
