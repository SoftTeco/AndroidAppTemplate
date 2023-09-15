package com.softteco.template.ui.feature.signUp

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
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
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.SimpleFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
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
    onBackClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    TextSnackbarContainer(
        modifier = Modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Column(
            modifier = modifier,
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
            ) {
                if (state.loading) {
                    Text(stringResource(id = R.string.loading))
                }
                UserNameField(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                )
                EmailField(state = state, modifier = Modifier.fillMaxWidth())
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
    modifier: Modifier = Modifier,
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

@Composable
private fun EmailField(
    state: SignUpViewModel.State,
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

@Composable
private fun PasswordField(
    state: SignUpViewModel.State,
    modifier: Modifier = Modifier,
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
                        imageVector = if (passwordVisibility) {
                            Icons.Default.Create
                        } else {
                            Icons.Default.Done
                        },
                        contentDescription = stringResource(id = R.string.visibility),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
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
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        targetValue = if (check) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        label = "ColorAnimation"
    )

    val icon = if (check) {
        Icons.Rounded.Check
    } else {
        Icons.Rounded.Close
    }

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
            SignUpViewModel.State()
        )
    }
}
