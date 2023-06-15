package com.softteco.template.presentation.login.loginComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.softteco.template.presentation.R
import com.softteco.template.presentation.common.Constants.EMAIL_PATTERN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailFieldComponent(
    fieldName: MutableState<TextFieldValue>,
    fieldNameErrorState: MutableState<Boolean>,
    fieldNameStr: Int
) {
    OutlinedTextField(
        value = fieldName.value,
        onValueChange = {
            if (fieldNameErrorState.value) {
                fieldNameErrorState.value = false
            }
            fieldName.value = it
        },

        modifier = Modifier.fillMaxWidth(),
        isError = fieldNameErrorState.value,
        label = {
            Text(text = stringResource(id = fieldNameStr))
        },
    )

    if (fieldNameErrorState.value) {
        Text(text = stringResource(id = R.string.required), color = Color.Red)
    } else if (fieldName.value.text.isNotEmpty() && !EMAIL_PATTERN.toRegex()
            .matches(fieldName.value.text)
    ) {
        Text(text = "Invalid email format", color = Color.Red)
        fieldNameErrorState.value = true
    }
}