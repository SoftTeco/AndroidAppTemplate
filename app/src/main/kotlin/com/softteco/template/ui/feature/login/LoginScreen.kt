package com.softteco.template.ui.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EmailField
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

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
				EmailField(
					strId = R.string.email,
					state.email,
					state.fieldStateEmail.textId,
					state.fieldStateEmail.color,
					state.fieldStateEmail.show,
					state.fieldStateEmail.isEmailValid,
					state.fieldStateEmail.emailNotValidTextId,
					modifier = Modifier.fillMaxWidth(),
					onFieldValueChanged = state.onEmailChanged
				)
				PasswordField(
					strId = R.string.password,
					state.password,
					state.fieldStatePassword.textId,
					state.fieldStatePassword.color,
					state.fieldStatePassword.show,
					modifier = Modifier.fillMaxWidth(),
					onNameChanged = state.onPasswordChanged
				)
				Box(
					modifier = Modifier.padding(
						Dimens.PaddingLarge, 0.dp, Dimens.PaddingLarge, 0.dp
					)
				) {
					Button(
						onClick = {
							state.onLoginClicked()
							if (state.loginState) {
								onLoginClicked() // transfer to user's screen
							}
						},
						shape = RoundedCornerShape(Dimens.PaddingBig),
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
					style = TextStyle(
						fontSize = 20.sp,
						fontFamily = FontFamily.Default,
						textDecoration = TextDecoration.Underline,
						color = Color.Blue
					)
				)
			}
		}
	}
}

@Preview
@Composable
private fun Preview() {
	AppTheme {
		ScreenContent(
			LoginViewModel.State(
				true,
				true,
				" ",
				" ",
				true,
				true,
				true,
				SnackBarState(
					textId = 0,
					show = false
				),
			)
		)
	}
}