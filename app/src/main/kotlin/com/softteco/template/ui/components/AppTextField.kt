package com.softteco.template.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
    fieldState: FieldState,
    onInputComplete: () -> Unit,
    @StringRes
    labelRes: Int,
    modifier: Modifier = Modifier,
) {
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }
    val isError = fieldState is FieldState.Error

    OnHideKeyboard { onInputComplete() }

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
                    is FieldState.Empty -> stringResource(R.string.required)
                    is FieldState.EmailError -> stringResource(fieldState.errorRes)
                    is FieldState.UsernameError -> stringResource(fieldState.errorRes)
                    else -> ""
                },
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

@Composable
fun OnHideKeyboard(action: () -> Unit) {
    val windowInsets = WindowInsets.ime
    val density = LocalDensity.current
    val isKeyboardVisible by remember {
        derivedStateOf { windowInsets.getBottom(density) > 0 }
    }

    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible && isInitialized) action()
        isInitialized = true
    }
}
