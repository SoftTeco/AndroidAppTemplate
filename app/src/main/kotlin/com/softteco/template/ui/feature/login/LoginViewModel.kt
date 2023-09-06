package com.softteco.template.ui.feature.login

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.Constants
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.LoginAuthDto

import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.ValidateFields
import com.softteco.template.ui.feature.ValidateFields.isEmailCorrect
import com.softteco.template.ui.feature.ValidateFields.isFieldEmpty
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
	private var emailStateValue = MutableStateFlow("")
	private var passwordStateValue = MutableStateFlow("")
	private var snackBarState = MutableStateFlow(SnackBarState())

	val state = combine(
		loading,
		loginState,
		emailStateValue,
		passwordStateValue,
		snackBarState
	) { loading, loginState, emailValue, passwordValue, snackBar ->
		State(
			loading = loading,
			emailValue = emailValue,
			passwordValue = passwordValue,
			snackBar = snackBar,
			dismissSnackBar = { snackBarState.value = SnackBarState() },
			onEmailChanged = { emailStateValue.value = it },
			onPasswordChanged = { passwordStateValue.value = it },
			fieldStateEmail = when {
		emailValue.isEmailCorrect() -> EmailFieldState.Success
			emailValue.isFieldEmpty() -> EmailFieldState.Empty
				else -> EmailFieldState.Error(R.string.email_not_valid)
			},
			fieldStatePassword = when {
			passwordValue.isFieldEmpty()-> PasswordFieldState.Empty
				else -> PasswordFieldState.Success
			},
			onLoginClicked = ::onLogin,
		)
	}.stateIn(
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

	private fun onLogin() = viewModelScope.launch {
		loading.value = true
		val userAuthDto = LoginAuthDto(
			email = emailStateValue.value,
			password = passwordStateValue.value
		)
		repository.login(userAuthDto).run {
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
		val emailValue: String = "",
		val passwordValue: String = "",
		val fieldStateEmail: EmailFieldState = EmailFieldState.Waiting(R.string.required),
		val fieldStatePassword: PasswordFieldState = PasswordFieldState.Waiting(R.string.required),
		val snackBar: SnackBarState = SnackBarState(),
		val onEmailChanged: (String) -> Unit = {},
		val onPasswordChanged: (String) -> Unit = {},
		val onLoginClicked: () -> Unit = {},
		val dismissSnackBar: () -> Unit = {}
	)
}

sealed class EmailFieldState {
	object Success : EmailFieldState()
	object Empty : EmailFieldState()
	class Waiting(@StringRes val labelRes: Int?) : EmailFieldState()
	class Error(@StringRes val labelRes: Int?) : EmailFieldState()
}

sealed class PasswordFieldState {
	object Success : PasswordFieldState()
	object Empty : PasswordFieldState()
	class Waiting(@StringRes val labelRes: Int) : PasswordFieldState()
	class Error(@StringRes val labelRes: Int) : PasswordFieldState()
}
