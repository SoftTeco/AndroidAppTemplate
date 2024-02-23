package com.softteco.template.ui.feature.resetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.Dimens.PaddingExtraLarge
import com.softteco.template.utils.Analytics

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Analytics.resetPasswordOpened()
    LaunchedEffect(state.resetPasswordState) {
        if (state.resetPasswordState is ScreenState.Success) {
            Analytics.resetPasswordSuccess()
            onSuccess()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    ScreenContent(
        state = state,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { keyboardController?.hide() })
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScreenContent(
    state: ResetPasswordViewModel.State,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(PaddingExtraLarge)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.enter_new_password))
        PasswordField(
            passwordValue = state.password.text,
            onPasswordChanged = state.onPasswordChanged,
            fieldStatePassword = state.password.state,
            onInputComplete = state.onInputComplete,
            modifier = Modifier
                .padding(top = PaddingExtraLarge)
                .fillMaxWidth()
        )

        val keyboardController = LocalSoftwareKeyboardController.current
        PrimaryButton(
            buttonText = stringResource(id = R.string.reset_password),
            loading = state.resetPasswordState == ScreenState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.PaddingDefault),
            enabled = state.isResetBtnEnabled,
            onClick = {
                state.onResetPasswordClicked()
                keyboardController?.hide()
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            ResetPasswordViewModel.State()
        )
    }
}
