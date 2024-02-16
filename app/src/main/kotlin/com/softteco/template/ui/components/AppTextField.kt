package com.softteco.template.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.softteco.template.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    fieldState: TextFieldState,
    onInputComplete: () -> Unit,
    @StringRes
    labelRes: Int,
    modifier: Modifier = Modifier,
) {
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }
    val isError = fieldState is TextFieldState.Error

    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    LaunchedEffect(isKeyboardVisible) { if (!isKeyboardVisible) onInputComplete() }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChanged(newValue)
            },
            modifier = modifier.onFocusChanged {
                isFocused = it.isFocused
                if (!it.isFocused) onInputComplete()
            },
            label = {
                Text(text = stringResource(labelRes))
            },
            isError = isError,
            supportingText = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = when (fieldState) {
                        is TextFieldState.Empty -> stringResource(R.string.required)
                        is TextFieldState.EmailError -> stringResource(fieldState.errorRes)
                        is TextFieldState.UsernameError -> stringResource(fieldState.errorRes)
                        else -> ""
                    },
                    color = if (isError) MaterialTheme.colorScheme.error else LocalContentColor.current
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                onInputComplete()
            }),
            singleLine = true,
        )
    }
}
