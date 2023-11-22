package com.softteco.template.ui.feature.resetPassword

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.theme.Dimens.PaddingExtraLarge

@Composable
fun ResetPasswordScreen(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.resetPasswordState) {
        if (state.resetPasswordState is ResetPasswordViewModel.ResetPasswordState.Success) {
            onSuccess()
        }
    }

    ScreenContent(
        state = state,
        modifier = modifier,
    )
}

@Composable
private fun ScreenContent(
    state: ResetPasswordViewModel.State,
    modifier: Modifier = Modifier,
) {
    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(PaddingExtraLarge)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.enter_new_password))
            PasswordField(
                passwordValue = state.passwordValue,
                onPasswordChanged = state.onPasswordChanged,
                fieldStatePassword = state.fieldStatePassword,
                modifier = Modifier
                    .padding(top = PaddingExtraLarge)
                    .fillMaxWidth()
            )
            PrimaryButton(
                buttonText = stringResource(id = R.string.reset_password),
                loading = state.resetPasswordState == ResetPasswordViewModel.ResetPasswordState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.PaddingDefault),
                enabled = state.isResetBtnEnabled,
                onClick = { state.onResetPasswordClicked() }
            )
        }
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
