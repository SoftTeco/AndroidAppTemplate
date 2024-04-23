package com.softteco.template.ui.feature.forgotPassword

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.AppTextField
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.utils.Analytics
import com.softteco.template.utils.LockScreenOrientation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ForgotPasswordScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    LockScreenOrientation(Configuration.ORIENTATION_PORTRAIT)

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        Analytics.forgotPasswordOpened()

        viewModel.navDestination.onEach { screen ->
            when (screen) {
                Screen.Login -> onSuccess()
                Screen.SignUp -> navigateToSignUp()

                else -> { /*NOOP*/
                }
            }
        }.launchIn(this)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    ScreenContent(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { keyboardController?.hide() })
        },
        state = state,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScreenContent(
    state: ForgotPasswordViewModel.State,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            stringResource(id = R.string.forgot_password),
            showBackIcon = true,
            modifier = Modifier.fillMaxWidth(),
            onBackClicked = onBackClicked
        )
        Column(
            modifier = Modifier.padding(Dimens.PaddingNormal),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextField(
                value = state.email.text,
                onValueChanged = state.onEmailChanged,
                fieldState = state.email.state,
                onInputComplete = state.onInputComplete,
                labelRes = R.string.email,
                modifier = Modifier.fillMaxWidth()
            )

            val keyboardController = LocalSoftwareKeyboardController.current
            PrimaryButton(
                buttonText = stringResource(id = R.string.restore_password),
                loading = state.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.PaddingLarge),
                enabled = state.isResetBtnEnabled,
                onClick = {
                    state.onRestorePasswordClicked()
                    keyboardController?.hide()
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(state = ForgotPasswordViewModel.State(), onBackClicked = {})
    }
}
