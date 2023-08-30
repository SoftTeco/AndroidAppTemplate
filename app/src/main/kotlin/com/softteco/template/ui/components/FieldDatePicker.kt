package com.softteco.template.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.softteco.template.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun FieldDatePicker(
    onResDate: String,
    fieldErrorState: Boolean,
    fieldNameStr: Int,
    modifier: Modifier = Modifier,
    onFieldValueChanged: ((String) -> Unit) = {}
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        }
    ) {
        datepicker { date ->
            onFieldValueChanged(date.toString())
        }
    }
    val source = remember {
        MutableInteractionSource()
    }

    OutlinedTextField(
        value = onResDate,
        onValueChange = {
            onFieldValueChanged(it)
        },
        readOnly = true,
        modifier = modifier,
        isError = fieldErrorState,
        label = {
            Text(text = stringResource(id = fieldNameStr))
        },
        interactionSource = source
    )
    if (fieldErrorState) {
        Text(text = stringResource(id = R.string.required), color = Color.Red)
    }
    if (source.collectIsPressedAsState().value) {
        dialogState.show()
    }
}
