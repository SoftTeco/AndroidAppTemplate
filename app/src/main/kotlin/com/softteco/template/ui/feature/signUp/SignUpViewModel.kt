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
	private val registrationState = MutableStateFlow(false)
	private var userNameStateValue = MutableStateFlow("")
	private var emailStateValue = MutableStateFlow("")
	private var passwordStateValue = MutableStateFlow("")
	private var snackBarState = MutableStateFlow(SnackBarState())

	fun combineState(
		loadingFlow: MutableStateFlow<Boolean>,
		registrationStateFlow: MutableStateFlow<Boolean>,
		userNameFlow: MutableStateFlow<String>,
		emailFlow: MutableStateFlow<String>,
		passwordFlow: MutableStateFlow<String>,
		snackBarFlow: MutableStateFlow<SnackBarState>
	): Flow<State> {
		return combine(
			loadingFlow,
			registrationStateFlow,
			userNameFlow,
			emailFlow,
			passwordFlow,
			snackBarFlow
		) { array ->
			val loading = array[0] as Boolean
			val registrationState = array[1] as Boolean
			val userName = array[2] as String
			val email = array[3] as String
			val password = array[4] as String
			val snackBar = array[5] as SnackBarState
			State(
				loading = loading,
				registrationState = registrationState,
				userNameValue = userName,
				emailValue = email,
				passwordValue = password,
				fieldStateUserName = when {
					userName.isFieldEmpty() -> SimpleFieldState.Empty
					else -> SimpleFieldState.Success
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
				snackBar = snackBar,
				dismissSnackBar = { snackBarFlow.value = SnackBarState() },
				onUserNameChanged = { userNameFlow.value = it },
				onEmailChanged = { emailFlow.value = it },
				onPasswordChanged = { passwordFlow.value = it },
				onRegisterClicked = ::onRegister
			)
		}
	}

	val stateFlow = combineState(
		loading,
		registrationState,
		userNameStateValue,
		emailStateValue,
		passwordStateValue,
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
		registrationState.value = false
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
			firstName = userNameStateValue.value,
			email = emailStateValue.value,
			password = passwordStateValue.value,
			confirmPassword = passwordStateValue.value,
		)
		repository.registration(createUserDto).run {
			when (this) {
				is Result.Success -> registrationState.value = true // FIXME
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
		val registrationState: Boolean = false,
		val userNameValue: String = "",
		val emailValue: String = "",
		val passwordValue: String = "",
		val fieldStateUserName: SimpleFieldState = SimpleFieldState.Waiting(R.string.required),
		val fieldStateEmail: EmailFieldState = EmailFieldState.Waiting(R.string.required),
		val fieldStatePassword: PasswordFieldState = PasswordFieldState.Waiting(R.string.required),
		val snackBar: SnackBarState = SnackBarState(),
		val onUserNameChanged: (String) -> Unit = {},
		val onEmailChanged: (String) -> Unit = {},
		val onPasswordChanged: (String) -> Unit = {},
		val onRegisterClicked: () -> Unit = {},
		val dismissSnackBar: () -> Unit = {}
	)
}
