package com.softteco.template.ui.feature.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EmailField
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.SimpleFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    ScreenContent(
        state = state,
        modifier = modifier,
        onBackClicked = onBackClicked
    )
}

@Composable
private fun ScreenContent(
    state: SignUpViewModel.State,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                Text(
                    stringResource(id = R.string.loading),
                    modifier = Modifier.alpha(if (state.loading) 1f else 0f)
                )
                UserNameField(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                )
                EmailField(
                    state.emailValue,
                    state.onEmailChanged,
                    state.fieldStateEmail,
                    Modifier.fillMaxWidth()
                )
                PasswordField(
                    state.passwordValue,
                    state.onPasswordChanged,
                    state.fieldStatePassword,
                    state.isPasswordHasMinimum,
                    state.isPasswordHasUpperCase,
                    Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier.padding(
                        Dimens.PaddingNormal,
                        Dimens.PaddingNormal,
                        Dimens.PaddingNormal
                    )
                ) {
                    Button(
                        onClick = {
                            state.onRegisterClicked()
                            if (state.registrationState) {
                                // transfer to user's screen
                            }
                        },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.PaddingExtraLarge)
                    ) {
                        Text(text = stringResource(id = R.string.sign_up))
                    }
                }
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
            isError = state.fieldStateUserName is SimpleFieldState.Empty
        )
        val errorText = when (state.fieldStateUserName) {
            is SimpleFieldState.Empty -> stringResource(R.string.required)
            else -> ""
        }
        Text(errorText, color = MaterialTheme.colorScheme.error)
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            SignUpViewModel.State()
        )
    }
}
