package com.softteco.template.ui.feature.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EmailField
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.Dimens.PaddingDefault

@Composable
fun SignUpScreen(
    onBackClicked: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.registrationState) {
        if (state.registrationState is SignUpViewModel.SignupState.Success) {
            onSuccess()
        }
    }

    ScreenContent(
        state = state,
        modifier = modifier,
        onBackClicked = onBackClicked,
    )
}

@Composable
private fun ScreenContent(
    state: SignUpViewModel.State,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTopAppBar(
                stringResource(id = R.string.sign_up),
                showBackIcon = true,
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = onBackClicked
            )
            Column(
                modifier = Modifier
                    .padding(Dimens.PaddingNormal)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserNameField(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                )
                EmailField(
                    emailValue = state.emailValue,
                    onEmailChanged = state.onEmailChanged,
                    fieldStateEmail = state.fieldStateEmail,
                    modifier = Modifier
                        .padding(top = PaddingDefault)
                        .fillMaxWidth()
                )
                PasswordField(
                    passwordValue = state.passwordValue,
                    onPasswordChanged = state.onPasswordChanged,
                    fieldStatePassword = state.fieldStatePassword,
                    modifier = Modifier
                        .padding(top = PaddingDefault)
                        .fillMaxWidth()
                )
                PrimaryButton(
                    buttonText = stringResource(id = R.string.sign_up),
                    loading = state.registrationState == SignUpViewModel.SignupState.Loading,
                    modifier = Modifier
                        .padding(top = PaddingDefault)
                        .fillMaxWidth(),
                    enabled = state.isSignupBtnEnabled,
                    onClick = { state.onRegisterClicked() }
                )
            }
        }
    }
}

@Composable
private fun UserNameField(
    state: SignUpViewModel.State,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            value = state.userNameValue,
            onValueChange = {
                state.onUserNameChanged(it)
            },
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.user_name))
            },
            supportingText = {
                Text(if (state.userNameValue.isBlank()) stringResource(R.string.required) else "")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Ascii,
            ),
            singleLine = true,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            SignUpViewModel.State(fieldStatePassword = PasswordFieldState.Error(true, false)),
            onBackClicked = {},
        )
    }
}
