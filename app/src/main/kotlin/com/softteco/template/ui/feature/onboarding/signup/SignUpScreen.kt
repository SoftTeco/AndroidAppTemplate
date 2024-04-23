package com.softteco.template.ui.feature.onboarding.signup

import android.content.res.Configuration
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.Constants.TERMS_OF_SERVICES_URL
import com.softteco.template.R
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.AppLinkText
import com.softteco.template.ui.components.AppTextField
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingExtraLarge
import com.softteco.template.ui.theme.Dimens.PaddingNormal
import com.softteco.template.utils.Analytics
import com.softteco.template.utils.LockScreenOrientation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    LockScreenOrientation(Configuration.ORIENTATION_PORTRAIT)

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        Analytics.signUpOpened()

        viewModel.navDestination.onEach { screen ->
            if (screen == Screen.Login) {
                Analytics.signUpSuccess()
                onSuccess()
            }
        }.launchIn(this)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    ScreenContent(
        state = state,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { keyboardController?.hide() })
        },
        onBackClicked = onBackClicked,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScreenContent(
    state: SignUpViewModel.State,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(PaddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTopAppBar(
            stringResource(id = R.string.sign_up),
            showBackIcon = true,
            modifier = Modifier.fillMaxWidth(),
            onBackClicked = onBackClicked
        )
        Column(
            modifier = Modifier.padding(PaddingNormal),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTextField(
                value = state.username.text,
                onValueChanged = state.onUsernameChanged,
                fieldState = state.username.state,
                onInputComplete = { state.onInputComplete(FieldType.USERNAME) },
                labelRes = R.string.username,
                modifier = Modifier
                    .padding(top = PaddingDefault)
                    .fillMaxWidth()
            )
            AppTextField(
                value = state.email.text,
                onValueChanged = state.onEmailChanged,
                fieldState = state.email.state,
                onInputComplete = { state.onInputComplete(FieldType.EMAIL) },
                labelRes = R.string.email,
                modifier = Modifier
                    .padding(top = PaddingDefault)
                    .fillMaxWidth()
            )
            PasswordField(
                passwordValue = state.password.text,
                onPasswordChanged = state.onPasswordChanged,
                fieldStatePassword = state.password.state,
                onInputComplete = { state.onInputComplete(FieldType.PASSWORD) },
                modifier = Modifier
                    .padding(top = PaddingDefault)
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.isTermsAccepted,
                    onCheckedChange = state.onCheckTermsChange,
                )
                AppLinkText(
                    text = stringResource(id = R.string.accept_terms_conditions),
                    linkText = stringResource(id = R.string.terms_conditions),
                    linkUrl = TERMS_OF_SERVICES_URL,
                    openLink = {
                        val intent = CustomTabsIntent.Builder().build()
                        intent.launchUrl(context, Uri.parse(it))
                    }
                )
            }

            val keyboardController = LocalSoftwareKeyboardController.current
            PrimaryButton(
                buttonText = stringResource(id = R.string.sign_up),
                loading = state.signUpState.loading,
                modifier = Modifier
                    .padding(top = PaddingDefault)
                    .fillMaxWidth(),
                enabled = state.isSignupBtnEnabled,
                onClick = {
                    state.onRegisterClicked()
                    keyboardController?.hide()
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            SignUpViewModel.State(
                password = TextFieldState(
                    "password",
                    FieldState.PasswordError(
                        isRightLength = true,
                        isUppercase = false
                    )
                ),
            ),
            onBackClicked = {},
        )
    }
}
