package com.softteco.template.ui.feature.resetPassword

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.PasswordFieldState
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
            PasswordField(state = state, modifier = Modifier.fillMaxWidth())
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

@Composable
private fun PasswordField(
    state: ResetPasswordViewModel.State,
    modifier: Modifier = Modifier
) {
    Column {
        var passwordVisibility by remember { mutableStateOf(true) }
        OutlinedTextField(
            value = state.passwordValue,
            onValueChange = {
                state.onPasswordChanged(it)
            },
            modifier = modifier,
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            isError = state.fieldStatePassword is PasswordFieldState.Empty,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.visibility),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
        )
        val errorText = when (state.fieldStatePassword) {
            PasswordFieldState.Empty -> stringResource(R.string.required)
            else -> ""
        }
        Text(errorText, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(Dimens.PaddingDefault))
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingDefault)) {
            ConditionRow(
                condition = stringResource(R.string.registration_password_condition1),
                check = state.isPasswordHasMinimum
            )
            ConditionRow(
                condition = stringResource(R.string.registration_password_condition2),
                check = state.isPasswordHasUpperCase
            )
        }
    }
}

@Composable
fun ConditionRow(
    condition: String,
    check: Boolean,
    modifier: Modifier = Modifier
) {
    val color by animateColorAsState(
        targetValue = if (check) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        label = "ColorAnimation"
    )

    val icon = if (check) Icons.Rounded.Check else Icons.Rounded.Close

    Row(modifier = modifier) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = stringResource(id = R.string.visibility)
        )
        Spacer(modifier = Modifier.width(Dimens.PaddingDefault))
        Text(
            text = condition,
            color = color
        )
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
