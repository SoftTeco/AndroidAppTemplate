package com.softteco.template.ui.feature.forgotPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EmailField
import com.softteco.template.ui.components.PrimaryButton
import com.softteco.template.ui.components.TextSnackbarContainer
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
                EmailField(
                    emailValue = state.emailValue,
                    onEmailChanged = state.onEmailChanged,
                    fieldStateEmail = state.fieldStateEmail,
                    modifier = Modifier.fillMaxWidth()
                )
                PrimaryButton(
                    buttonText = stringResource(id = R.string.restore_password),
                    loading = state.loading,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { state.onRestorePasswordClicked() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(state = ForgotPasswordViewModel.State())
    }
}
