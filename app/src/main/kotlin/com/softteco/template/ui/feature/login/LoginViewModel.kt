package com.softteco.template.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.LoginAuthDto
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
	val loginState = MutableStateFlow(false)

	var emailState = MutableStateFlow<String>("")
	var passwordState = MutableStateFlow<String>("")
	var fieldValidationState = MutableStateFlow(ValidateFields())
	var snackBarState = MutableStateFlow(SnackBarState())
	fun login(
		userAuth: LoginAuthDto
	) = viewModelScope.launch {
		loading.value = true
		repository.login(userAuth).run {
			when (this) {
				is Result.Success -> loginState.value = true
				is Result.Error -> loginState.value = false
			}
		}
		loading.value = false
	}

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
			onLoginClicked = {
				viewModelScope.launch {
					repository.login(LoginAuthDto(emailValue, passwordValue))
				}
			}
		)
	}.stateIn(
		viewModelScope,
		SharingStarted.Lazily,
		State()
	)


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
		val onLoginClicked: () -> Unit = {},
		val dismissSnackBar: () -> Unit = {}
	)
}
