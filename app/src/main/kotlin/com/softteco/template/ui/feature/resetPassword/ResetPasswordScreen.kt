package com.softteco.template.ui.feature.resetPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    gotToLoginScreen: () -> Unit = {},
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    ScreenContent(
        state = state,
        modifier = modifier,
        gotToLoginScreen = gotToLoginScreen,
    )
}

@Composable
private fun ScreenContent(
    state: ResetPasswordViewModel.State,
    modifier: Modifier = Modifier,
    gotToLoginScreen: () -> Unit = {}
) {
    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.PaddingExtraLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.loading) {
                Text(stringResource(id = R.string.loading))
            }

            Text(text = stringResource(id = R.string.enter_new_password))
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
                        state.onResetPasswordClicked()
                        if (state.resetPasswordState) {
                            gotToLoginScreen()
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.PaddingExtraLarge)
                ) {
                    Text(text = stringResource(id = R.string.reset_password))
                }
            }
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
