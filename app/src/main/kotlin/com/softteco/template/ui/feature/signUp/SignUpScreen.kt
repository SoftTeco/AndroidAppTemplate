package com.softteco.template.ui.feature.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EmailField
import com.softteco.template.ui.components.SimpleField
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.components.TextFieldWithDropDownComponent
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.login.LoginViewModel
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

	var country by remember { mutableStateOf("") }
	val countryList = mutableListOf("Belarus", "Poland", "Italia", "Spain")

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
		)
		{
			CustomTopAppBar(
				stringResource(id = R.string.sign_up),
				showBackIcon = true,
				modifier = Modifier.fillMaxWidth(),
				onBackClicked = onBackClicked
			)
			Column(
				modifier = Modifier
					.padding(Dimens.Padding20)
					.verticalScroll(scrollState),
				verticalArrangement = Arrangement.Center,
			) {
				if (state.loading) {
					Text(stringResource(id = R.string.loading))
				}

				SimpleField(
					strId = R.string.first_name,
					state.firstNameValue,
					state.firstNameFieldState.textId,
					state.firstNameFieldState.color,
					state.firstNameFieldState.show,
					modifier = Modifier.fillMaxWidth(),
					onFieldValueChanged = state.firstNameChanged
				)

				SimpleField(
					strId = R.string.last_name,
					state.lastNameValue,
					state.lastNameFieldState.textId,
					state.lastNameFieldState.color,
					state.lastNameFieldState.show,
					modifier = Modifier.fillMaxWidth(),
					onFieldValueChanged = state.lastNameChanged
				)

				EmailField(
					strId = R.string.email,
					state.emailValue,
					state.fieldStateEmail.textId,
					state.fieldStateEmail.color,
					state.fieldStateEmail.show,
					state.fieldStateEmail.isEmailValid,
					state.fieldStateEmail.emailNotValidTextId,
					modifier = Modifier.fillMaxWidth(),
					onFieldValueChanged = state.onEmailChanged
				)


//				PasswordFieldComponentWithValidation(
//					modifier = Modifier.fillMaxWidth(),
//					value = viewModel.passwordValue,
//					fieldErrorState = viewModel.passwordValue.isEmpty(),
//					passwordError = passwordError,
//					onFieldValueChanged = { newValue ->
//						viewModel.passwordValue = newValue
//						viewModel.changePassword(newValue)
//					}
//				)

				Spacer(Modifier.size(Dimens.PaddingNormal))

//				PasswordField(
//					strId = R.string.confirm_password,
//					viewModel.confirmPasswordValue,
//					nameErrorState = viewModel.confirmPasswordValue.isEmpty(),
//					modifier = Modifier.fillMaxWidth(),
//					onNameChanged = { newValue -> viewModel.confirmPasswordValue = newValue }
//				)
//
//				if (viewModel.confirmPasswordValue.isNotEmpty() && viewModel.passwordValue.isNotEmpty()) {
//					if (viewModel.confirmPasswordValue != viewModel.passwordValue) {
//						Text(
//							text = stringResource(id = R.string.passwords_mismatching),
//							color = Color.Red
//						)
//					}
//				}

				Spacer(Modifier.size(Dimens.PaddingNormal))

				TextFieldWithDropDownComponent(
					item = country,
					strId = R.string.country,
					fieldErrorState = country.isEmpty(),
					itemsList = countryList,
					modifier = Modifier.fillMaxWidth(),
					onFieldValueChanged = { newValue -> country = newValue }
				)

				Spacer(Modifier.size(Dimens.PaddingNormal))

//				FieldDatePicker(
//					viewModel.birthDayValue,
//					viewModel.birthDayValue.isEmpty(),
//					R.string.birth_day,
//					modifier = Modifier.fillMaxWidth(),
//					onFieldValueChanged = { newValue -> viewModel.birthDayValue = newValue }
//				)

				Spacer(Modifier.size(Dimens.PaddingNormal))

				Box(
					modifier = Modifier.padding(
						Dimens.Padding40,
						Dimens.Padding0,
						Dimens.Padding40,
						Dimens.Padding0
					)
				) {
//					val isFieldsValid: Boolean =
//						viewModel.firstNameValue.isEmpty() || viewModel.lastNameValue.isEmpty()
//							|| viewModel.emailValue.isEmpty() || !state.emailError ||
//							viewModel.passwordValue.isEmpty() || viewModel.confirmPasswordValue.isEmpty()
//							|| viewModel.birthDayValue.isEmpty() ||
//							country.isEmpty() || !state.passwordError ||
//							viewModel.passwordValue != viewModel.confirmPasswordValue
					Button(
						onClick = {
//							if (isFieldsValid) {
//								Toast.makeText(
//									context,
//									context.getText(R.string.empty_fields_error),
//									Toast.LENGTH_SHORT
//								).show()
//							} else {
//								viewModel.register(
//									CreateUserDto(
//										viewModel.firstNameValue,
//										viewModel.lastNameValue,
//										viewModel.emailValue,
//										viewModel.passwordValue,
//										viewModel.confirmPasswordValue,
//										country,
//										viewModel.birthDayValue
//									)
//								)
//								if (viewModel.signUpState.value) {
//									Toast.makeText(
//										context,
//										context.getText(R.string.go_to_login),
//										Toast.LENGTH_SHORT
//									).show()
//									onBackClicked()
//								} else {
//									Toast.makeText(
//										context,
//										context.getText(R.string.problem_error),
//										Toast.LENGTH_SHORT
//									).show()
//								}
//							}
						},
						shape = RoundedCornerShape(Dimens.Padding50),
						modifier = Modifier
							.fillMaxWidth()
							.height(Dimens.Padding50)
					) {
						Text(text = stringResource(id = R.string.sign_up))
					}
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
			SignUpViewModel.State(
			)
		)
	}
}
