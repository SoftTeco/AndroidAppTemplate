package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto

import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.SimpleFieldState
import com.softteco.template.ui.feature.ValidateFields.isEmailCorrect
import com.softteco.template.ui.feature.ValidateFields.isFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val repository: ProfileRepository,
) : ViewModel() {
	private val loading = MutableStateFlow(false)
	private val loginState = MutableStateFlow(false)
	private var firstNameStateValue = MutableStateFlow("")
	private var lastNameStateValue = MutableStateFlow("")
	private var emailStateValue = MutableStateFlow("")
	private var passwordStateValue = MutableStateFlow("")
	private var confirmPasswordStateValue = MutableStateFlow("")
	private var countryStateValue = MutableStateFlow("")
	private var birthdayPasswordStateValue = MutableStateFlow("")
	private var snackBarState = MutableStateFlow(SnackBarState())

	fun combineState(
		loadingFlow: MutableStateFlow<Boolean>,
		loginStateFlow: MutableStateFlow<Boolean>,
		firstNameFlow: MutableStateFlow<String>,
		lastNameFlow: MutableStateFlow<String>,
		emailFlow: MutableStateFlow<String>,
		passwordFlow: MutableStateFlow<String>,
		confirmPasswordFlow: MutableStateFlow<String>,
		countryFlow: MutableStateFlow<String>,
		birthdayFlow: MutableStateFlow<String>,
		snackBarFlow: MutableStateFlow<SnackBarState>
	): Flow<State> {
		return combine(
			loadingFlow,
			loginStateFlow,
			firstNameFlow,
			lastNameFlow,
			emailFlow,
			passwordFlow,
			confirmPasswordFlow,
			countryFlow,
			birthdayFlow,
			snackBarFlow
		) { array ->
			val loading = array[0] as Boolean
			val loginState = array[1] as Boolean
			val firstName = array[2] as String
			val lastName = array[3] as String
			val email = array[4] as String
			val password = array[5] as String
			val confirmPassword = array[6] as String
			val country = array[7] as String
			val birthday = array[8] as String
			val snackBar = array[9] as SnackBarState
			State(
				loading = loading,
				loginState = loginState,
				firstNameValue = firstName,
				secondNameValue = lastName,
				emailValue = email,
				passwordValue = password,
				confirmedPasswordValue = confirmPassword,
				countryValue = country,
				birthDayValue = birthday,
				fieldStateFirstName = when {
					firstName.isFieldEmpty() -> SimpleFieldState.Empty
					else -> SimpleFieldState.Success
				},
				fieldStateSecondName = when {
					lastName.isFieldEmpty() -> SimpleFieldState.Empty
					else -> {
						SimpleFieldState.Success
					}
				},
				fieldStateEmail = when {
					email.isEmailCorrect() -> EmailFieldState.Success
					email.isFieldEmpty() -> EmailFieldState.Empty
					else -> EmailFieldState.Error(R.string.email_not_valid)
				},
				fieldStatePassword = when {
					password.isFieldEmpty() -> PasswordFieldState.Empty
					else -> PasswordFieldState.Success
				},
				fieldStateConfirmedPassword = when {
					confirmPassword.isFieldEmpty() -> PasswordFieldState.Empty
					else -> PasswordFieldState.Success
				},
				fieldStateCountry = when {
					country.isFieldEmpty() -> SimpleFieldState.Empty
					else -> {
						SimpleFieldState.Success
					}
				},
				fieldStateBirthDay = when {
					birthday.isFieldEmpty() -> SimpleFieldState.Empty
					else -> {
						SimpleFieldState.Success
					}
				},
				snackBar = snackBar,
				dismissSnackBar = { snackBarFlow.value = SnackBarState() },
				onFirstNameChanged = { firstNameFlow.value = it },
				onSecondNameChanged = { lastNameFlow.value = it },
				onEmailChanged = { emailFlow.value = it },
				onPasswordChanged = { passwordFlow.value = it },
				onConfirmedPasswordChanged = { confirmPasswordFlow.value = it },
				onCountryChanged = { countryFlow.value = it },
				onBirthChanged = { birthdayFlow.value = it },
				onRegisterClicked = ::onRegister
			)
		}
	}

	val stateFlow = combineState(
		loading,
		loginState,
		firstNameStateValue,
		lastNameStateValue,
		emailStateValue,
		passwordStateValue,
		confirmPasswordStateValue,
		countryStateValue,
		birthdayPasswordStateValue,
		snackBarState
	)

	val state = stateFlow.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)

	private fun handleError() {
		val isAllFieldsValid = state.value.run {
			fieldStateEmail is EmailFieldState.Success &&
				fieldStatePassword is PasswordFieldState.Success

		}
		loginState.value = false
		snackBarState.value = SnackBarState(
			if (isAllFieldsValid) {
				R.string.problem_error
			} else {
				R.string.empty_fields_error
			},
			true
		)
	}

	private fun onRegister() = viewModelScope.launch {
		loading.value = true
		val createUserDto = CreateUserDto(
			firstName = firstNameStateValue.value,
			lastName = lastNameStateValue.value,
			email = emailStateValue.value,
			password = passwordStateValue.value,
			confirmPassword = confirmPasswordStateValue.value,
			country = countryStateValue.value,
			birthday = birthdayPasswordStateValue.value
		)
		repository.registration(createUserDto).run {
			when (this) {
				is Result.Success -> loginState.value = true // FIXME
				is Result.Error -> {
					handleError()
				}
			}
		}
		loading.value = false
	}

	@Immutable
	data class State(
		val loading: Boolean = false,
		val loginState: Boolean = false,
		val firstNameValue: String = "",
		val secondNameValue: String = "",
		val emailValue: String = "",
		val passwordValue: String = "",
		val confirmedPasswordValue: String = "",
		val countryValue: String = "",
		val birthDayValue: String = "",
		val fieldStateFirstName: SimpleFieldState = SimpleFieldState.Waiting(R.string.required),
		val fieldStateSecondName: SimpleFieldState = SimpleFieldState.Waiting(R.string.required),
		val fieldStateEmail: EmailFieldState = EmailFieldState.Waiting(R.string.required),
		val fieldStatePassword: PasswordFieldState = PasswordFieldState.Waiting(R.string.required),
		val fieldStateConfirmedPassword: PasswordFieldState = PasswordFieldState.Waiting(R.string.required),
		val fieldStateCountry: SimpleFieldState = SimpleFieldState.Waiting(R.string.required),
		val fieldStateBirthDay: SimpleFieldState = SimpleFieldState.Waiting(R.string.required),
		val snackBar: SnackBarState = SnackBarState(),
		val onFirstNameChanged: (String) -> Unit = {},
		val onSecondNameChanged: (String) -> Unit = {},
		val onEmailChanged: (String) -> Unit = {},
		val onPasswordChanged: (String) -> Unit = {},
		val onConfirmedPasswordChanged: (String) -> Unit = {},
		val onCountryChanged: (String) -> Unit = {},
		val onBirthChanged: (String) -> Unit = {},
		val onRegisterClicked: () -> Unit = {},
		val dismissSnackBar: () -> Unit = {}
	)
}
