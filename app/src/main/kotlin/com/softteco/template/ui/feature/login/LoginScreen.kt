package com.softteco.template.ui.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
	modifier: Modifier = Modifier,
	viewModel: LoginViewModel = hiltViewModel(),
	onBackClicked: () -> Unit = {},
	onLoginClicked: () -> Unit = {},
	onSignUpClicked: () -> Unit = {}
) {
	val state by viewModel.state.collectAsState()

	ScreenContent(
		modifier = modifier,
		state = state,
		fieldState = fieldState,
		onBackClicked = onBackClicked,
		onLoginClicked = onLoginClicked,
		onSignUpClicked = onSignUpClicked
	)
}

@Composable
private fun ScreenContent(
	state: LoginViewModel.State,
	modifier: Modifier = Modifier,
	onBackClicked: () -> Unit = {},
	onLoginClicked: () -> Unit = {},
	onSignUpClicked: () -> Unit = {}
) {
	TextSnackbarContainer(
		modifier = modifier,
		snackbarText = stringResource(state.snackBar.textId),
		showSnackbar = state.snackBar.show,
		onDismissSnackbar = state.dismissSnackBar,
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(Dimens.PaddingBig),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			CustomTopAppBar(
				stringResource(id = R.string.login),
				showBackIcon = true,
				modifier = Modifier.fillMaxWidth(),
				onBackClicked = onBackClicked
			)
			Column(
				modifier = Modifier.padding(Dimens.PaddingLarge),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				if (state.loading) {
					Text(stringResource(id = R.string.loading))
				}
				EmailField(state = state, Modifier.fillMaxWidth())
				PasswordField(state = state, Modifier.fillMaxWidth())
				Box(
					modifier = Modifier.padding(
						Dimens.PaddingLarge, Dimens.PaddingBig, Dimens.PaddingLarge
					)
				) {
					Button(
						onClick = {
							state.onLoginClicked()
							if (state.loginState) {
								onLoginClicked() // transfer to user's screen
							}
						},
						shape = MaterialTheme.shapes.large,
						modifier = Modifier
							.fillMaxWidth()
							.height(Dimens.PaddingBig)

					) {
						Text(text = stringResource(id = R.string.login))
					}
				}
				ClickableText(
					text = AnnotatedString(stringResource(id = R.string.sign_up)),
					modifier = Modifier.padding(Dimens.PaddingNormal),
					onClick = {
						onSignUpClicked()
					},
					style = MaterialTheme.typography.bodyLarge
				)
			}
		}
	}
}

@Composable
private fun EmailField(
	state: LoginViewModel.State,
	modifier: Modifier = Modifier,
) {
	var isError by remember { mutableStateOf(false) }
	OutlinedTextField(
		value = state.emailValue,
		onValueChange = {
			state.onEmailChanged(it)
		},
		modifier = modifier,
		label = {
			Text(text = stringResource(id = R.string.email))
		},
		isError = state.fieldStateEmail is EmailFieldState.Empty || isError
	)
	if (state.fieldStateEmail is EmailFieldState.Empty) {
		Text(text = stringResource(R.string.required), color = MaterialTheme.colorScheme.error)
	}
	LaunchedEffect(state.emailValue) {
		delay(1000)
		isError = state.fieldStateEmail is EmailFieldState.Error
	}
	if (isError) {
		Text(
			text = stringResource(R.string.email_not_valid),
			color = MaterialTheme.colorScheme.error
		)
	}
}

@Composable
private fun PasswordField(
	state: LoginViewModel.State,
	modifier: Modifier = Modifier,
) {
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
	if (state.fieldStatePassword is PasswordFieldState.Empty) {
		Text(text = stringResource(R.string.required), color = MaterialTheme.colorScheme.error)
	}
}

@Preview
@Composable
private fun Preview() {
	AppTheme {
		ScreenContent(state = LoginViewModel.State())
	}
}