package com.softteco.template.ui.feature.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button


import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField


import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar

import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.SimpleFieldState
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import kotlinx.coroutines.delay

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
					.padding(Dimens.PaddingLarge)
					.verticalScroll(scrollState),
				verticalArrangement = Arrangement.Center,
			) {
				if (state.loading) {
					Text(stringResource(id = R.string.loading))
				}
				SimpleField(
					state.fieldStateFirstName,
					R.string.first_name,
					state.firstNameValue,
					state.onFirstNameChanged,
					modifier = modifier.fillMaxWidth(),
				)
				SimpleField(
					state.fieldStateSecondName,
					R.string.last_name,
					state.secondNameValue,
					state.onSecondNameChanged,
					modifier = modifier.fillMaxWidth(),
				)

				EmailField(state = state, modifier = modifier.fillMaxWidth())
				PasswordField(state = state, modifier = modifier.fillMaxWidth())
				PasswordField(state = state, modifier = modifier.fillMaxWidth())

				TextFieldWithDropDownComponent(
				state = state,
					modifier = modifier.fillMaxWidth(),
				)

				Box(
					modifier = Modifier.padding(
						40.dp, 0.dp, 40.dp, 0.dp
					)
				) {

					Button(
						onClick = {
						},
						shape = RoundedCornerShape(50.dp),
						modifier = Modifier
							.fillMaxWidth()
							.height(50.dp)
					) {
						Text(text = stringResource(id = R.string.sign_up))
					}
				}
			}
		}
	}
}

@Composable
private fun DatePickerField(state: SignUpViewModel.State,
                            modifier: Modifier = Modifier){
	OutlinedTextField(
		value = state.birthDayValue,
		onValueChange = {
			state.onBirthChanged(it)
		},

		readOnly = true,
		modifier = modifier.clickable {  },
		label = {
			Text(text = stringResource(id = R.string.birth_day))
		},
		isError = state.fieldStateBirthDay is SimpleFieldState.Empty,

	)
	if (state.fieldStateBirthDay is SimpleFieldState.Empty) {
		Text(text = stringResource(id = R.string.required), color = MaterialTheme.colorScheme.error)
	}

}

@Composable
private fun SimpleField(
	fieldState: SimpleFieldState,
	labelStr: Int,
	fieldValue: String,
	onFieldChanged: (String) -> Unit = {},
	modifier: Modifier = Modifier,
) {

	OutlinedTextField(
		value = fieldValue,
		onValueChange = {
			onFieldChanged(it)
		},
		modifier = modifier,
		label = {
			Text(text = stringResource(id = labelStr))
		},
		isError = fieldState is SimpleFieldState.Empty

	)
	if (fieldState is SimpleFieldState.Empty) {
		Text(text = stringResource(R.string.required), color = MaterialTheme.colorScheme.error)
	}
}

@Composable
private fun EmailField(
	state: SignUpViewModel.State,
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
	state: SignUpViewModel.State,
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
					tint = MaterialTheme.colorScheme.background
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

@Composable
fun TextFieldWithDropDownComponent(
	state: SignUpViewModel.State,
	modifier: Modifier = Modifier
) {
	val country by remember { mutableStateOf("") }
	val countryList = mutableListOf("Belarus", "Poland", "Italia", "Spain")

	val userSelectedString: (String) -> Unit = {
		state.onCountryChanged(it)
	}
	val isOpen = remember { mutableStateOf(false) }
	val openCloseOfDropDownList: (Boolean) -> Unit = {
		isOpen.value = it
	}
	Box(modifier = modifier) {
		Column {
			OutlinedTextField(
				value = country,
				onValueChange = {
					state.onCountryChanged(it)
				},
				modifier = Modifier.fillMaxWidth(),
				isError = state.fieldStateCountry is SimpleFieldState.Empty,
				label = {
					Text(text = stringResource(id = R.string.country))
				},
			)
			DropdownMenu(
				modifier = modifier,
				expanded = isOpen.value,
				onDismissRequest = { openCloseOfDropDownList(false) }
			) {
				countryList.forEach {
					DropdownMenuItem(text = { Text(text = it) }, onClick = {
						openCloseOfDropDownList(false)
						userSelectedString(it)
					})
				}
			}
		}
		Spacer(
			modifier = Modifier
				.matchParentSize()
				.background(Color.Transparent)
				.padding(10.dp)
				.clickable(onClick = { isOpen.value = true })
		)
	}
	if (state.fieldStateCountry is SimpleFieldState.Empty) {
		Text(text = stringResource(id = R.string.required), color = Color.Red)
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
