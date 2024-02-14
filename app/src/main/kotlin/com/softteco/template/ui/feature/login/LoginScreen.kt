package com.softteco.template.ui.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.softteco.template.R
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EmailField
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.components.SecondaryButton
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.utils.Analytics

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

    ScreenContent(
        modifier = modifier,
        state = state,
        onBackClicked = onBackClicked,
        onSignUpClicked = onSignUpClicked,
        onForgotPasswordClicked = onForgotPasswordClicked
    )
}

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
            EmailField(
                emailValue = state.emailValue,
                onEmailChanged = state.onEmailChanged,
                fieldStateEmail = state.fieldStateEmail,
                modifier = Modifier.fillMaxWidth()
            )
            PasswordField(
                passwordValue = state.passwordValue,
                onPasswordChanged = state.onPasswordChanged,
                fieldStatePassword = state.fieldStatePassword,
                modifier = Modifier.fillMaxWidth()
            )
            PrimaryButton(
                buttonText = stringResource(id = R.string.login),
                loading = state.screenState == ScreenState.Loading,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isLoginBtnEnabled,
                onClick = { state.onLoginClicked() },
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
