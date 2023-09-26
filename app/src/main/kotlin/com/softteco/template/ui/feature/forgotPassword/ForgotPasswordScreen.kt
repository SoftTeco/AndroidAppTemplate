package com.softteco.template.ui.feature.forgotPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    ScreenContent(
        modifier = modifier,
        state = state,
        onBackClicked = onBackClicked
    )
}

@Composable
private fun ScreenContent(
    state: ForgotPasswordViewModel.State,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {}
) {
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
                Text(
                    stringResource(id = R.string.loading),
                    modifier = Modifier.alpha(if (state.loading) 1f else 0f)
                )
                EmailField(state = state, Modifier.fillMaxWidth())
                Box(
                    modifier = Modifier.padding(
                        Dimens.PaddingNormal,
                        Dimens.PaddingLarge,
                        Dimens.PaddingNormal
                    )
                ) {
                    Button(
                        onClick = {
                            state.onRestorePasswordClicked()
                        },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.PaddingExtraLarge)
                    ) {
                        Text(text = stringResource(id = R.string.restore_password))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmailField(
    state: ForgotPasswordViewModel.State,
    modifier: Modifier = Modifier,
) {
    Column {
        OutlinedTextField(
            value = state.emailValue,
            onValueChange = {
                state.onEmailChanged(it)
            },
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            isError = state.fieldStateEmail is EmailFieldState.Empty || state.fieldStateEmail is EmailFieldState.Error
        )
        val errorText = when (state.fieldStateEmail) {
            is EmailFieldState.Empty -> stringResource(R.string.required)
            is EmailFieldState.Error -> stringResource(R.string.email_not_valid)
            else -> ""
        }
        Text(errorText, color = MaterialTheme.colorScheme.error)
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(state = ForgotPasswordViewModel.State())
    }
}
