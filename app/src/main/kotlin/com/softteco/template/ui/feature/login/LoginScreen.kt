package com.softteco.template.ui.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.softteco.template.R
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.AppTextField
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.components.SecondaryButton
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.utils.Analytics

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    onSignUpClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    Analytics.logInOpened()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) viewModel.onCreate()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(state.screenState) {
        when (val screenState = state.screenState) {
            ScreenState.Success -> {
                Analytics.logInSuccess()
                onSuccess()
            }

            is ScreenState.Navigate -> {
                if (screenState.screen == Screen.SignUp) {
                    Analytics.signUpOpened()
                    onSignUpClicked()
                }
            }

            else -> { /*NOOP*/
            }
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    ScreenContent(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { keyboardController?.hide() })
        },
        state = state,
        onBackClicked = onBackClicked,
        onSignUpClicked = onSignUpClicked,
        onForgotPasswordClicked = onForgotPasswordClicked
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScreenContent(
    state: LoginViewModel.State,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color =
        if (isPressed) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            stringResource(id = R.string.login),
            modifier = Modifier.fillMaxWidth(),
            onBackClicked = onBackClicked
        )
        Column(
            modifier = Modifier.padding(Dimens.PaddingNormal),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingDefault),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextField(
                value = state.email.text,
                onValueChanged = state.onEmailChanged,
                fieldState = state.email.state,
                onInputComplete = { state.onInputComplete(FieldType.EMAIL) },
                labelRes = R.string.email,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                passwordValue = state.password.text,
                onPasswordChanged = state.onPasswordChanged,
                fieldStatePassword = state.password.state,
                onInputComplete = { state.onInputComplete(FieldType.PASSWORD) },
                modifier = Modifier.fillMaxWidth()
            )

            val keyboardController = LocalSoftwareKeyboardController.current
            PrimaryButton(
                buttonText = stringResource(id = R.string.login),
                loading = state.screenState == ScreenState.Loading,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isLoginBtnEnabled,
                onClick = {
                    state.onLoginClicked()
                    keyboardController?.hide()
                },
            )

            SecondaryButton(
                title = stringResource(id = R.string.sign_up),
                loading = false,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onSignUpClicked() }
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingDefault))
            Text(
                text = stringResource(id = R.string.forgot_password),
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onForgotPasswordClicked()
                },
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = TextDecoration.Underline,
                    color = color
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(state = LoginViewModel.State())
    }
}
