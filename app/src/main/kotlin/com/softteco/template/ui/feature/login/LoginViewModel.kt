package com.softteco.template.ui.feature.login
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.LoginAuthDto
import com.softteco.template.ui.components.EmailFieldState
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.ValidateFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val repository: ProfileRepository,
) : ViewModel() {


	private val loading = MutableStateFlow(false)
	private val loginState = MutableStateFlow(false)

	private var emailState = MutableStateFlow("")
	private var passwordState = MutableStateFlow("")

	private var isAllFieldsValid = MutableStateFlow(false)

	private var fieldValidationState = MutableStateFlow(ValidateFields())

	private var snackBarState = MutableStateFlow(SnackBarState())
	private var emailFieldState = MutableStateFlow(EmailFieldState())

	val state = combine(
		loading,
		loginState,
		emailState,
		passwordState,
		snackBarState
	) { loading, loginState, emailValue, passwordValue, snackBar ->
		State(
			loading = loading,
			loginState = loginState,
			email = emailValue,
			password = passwordValue,
			isEmailFieldEmpty = fieldValidationState.value.validateFieldEmpty(emailValue).isEmpty,
			isEmailFieldValid = fieldValidationState.value.validateEmail(emailValue).isEmailCorrect,
			isPasswordFieldEmpty = fieldValidationState.value.validateFieldEmpty(passwordValue).isEmpty,
			snackBar = snackBar,
			dismissSnackBar = { snackBarState.value = SnackBarState() },
			onEmailChanged = { emailState.value = it },
			onPasswordChanged = { passwordState.value = it },
			isAllFieldsValid = isAllFieldsValid.value,
			onLoginClicked = {
				login(
					userAuth = LoginAuthDto(
						email = emailState.value,
						password = passwordState.value
					)
				)
			},
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)

	val fieldState = combine(emailFieldState, emailState) { fieldState ->
		FieldState(
			fieldState = EmailFieldState(
				R.string.required, Color.Red,
				fieldValidationState.value.validateFieldEmpty(emailState.value).isEmpty,
				!fieldValidationState.value.validateFieldEmpty(emailState.value).isEmpty
					&& !fieldValidationState.value.validateEmail(
					emailState.value
				).isEmailCorrect, R.string.email_not_valid
			)
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		FieldState()
	)

	private fun handleError() {
		snackBarState.value = SnackBarState(
			if (isAllFieldsValid.value) {
				R.string.problem_error
			} else {
				R.string.empty_fields_error
			},
			true
		)
	}

	fun login(
		userAuth: LoginAuthDto
	) = viewModelScope.launch {
		loading.value = true
		repository.login(userAuth).run {
			when (this) {
				is Result.Success -> loginState.value = true
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
		val email: String = "",
		val password: String = "",
		val isEmailFieldEmpty: Boolean = false,
		val isEmailFieldValid: Boolean = false,
		val isPasswordFieldEmpty: Boolean = false,
		val snackBar: SnackBarState = SnackBarState(),
		val onEmailChanged: (String) -> Unit = {},
		val onPasswordChanged: (String) -> Unit = {},
		val isAllFieldsValid: Boolean = isEmailFieldValid && !isPasswordFieldEmpty && !isEmailFieldEmpty,
		val onLoginClicked: () -> Unit = {},
		val dismissSnackBar: () -> Unit = {}
	)

	@Immutable
	data class FieldState(
		val fieldState: EmailFieldState = EmailFieldState(),
		val email: String = "",
	)
}
