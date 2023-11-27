package com.softteco.template.ui.feature.login

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.validateEmail
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.handleApiError
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
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val loginState = MutableStateFlow<LoginState>(LoginState.Default)
    private var emailStateValue = MutableStateFlow("")
    private var passwordStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val emailFieldState = MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

    val state = combine(
        loginState,
        emailStateValue,
        passwordStateValue,
        snackBarState,
        emailFieldState
    ) { loginState, emailValue, passwordValue, snackBar, emailState ->
        State(
            loginState = loginState,
            emailValue = emailValue,
            passwordValue = passwordValue,
            snackBar = snackBar,
            isLoginBtnEnabled = emailState is EmailFieldState.Success && passwordValue.isNotBlank(),
            fieldStateEmail = emailState,
            fieldStatePassword = when {
                passwordValue.isEmpty() -> PasswordFieldState.Empty
                else -> PasswordFieldState.Success
            },
            onEmailChanged = {
                emailStateValue.value = it.trim()
                validateEmail(emailStateValue, emailFieldState, viewModelScope, appDispatchers)
            },
            onPasswordChanged = { passwordStateValue.value = it },
            onLoginClicked = ::onLogin,
            dismissSnackBar = { snackBarState.value = SnackBarState() },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    private fun onLogin() {
        viewModelScope.launch(appDispatchers.io) {
            loginState.value = LoginState.Loading

            val credentials = CredentialsDto(
                email = emailStateValue.value,
                password = passwordStateValue.value
            )

            val result = repository.login(credentials)
            loginState.value = when (result) {
                is Result.Success -> LoginState.Success
                is Result.Error -> {
                    handleApiError(result, snackBarState)
                    LoginState.Default
                }
            }
        }
    }

    @Immutable
    data class State(
        val loginState: LoginState = LoginState.Default,
        val snackBar: SnackBarState = SnackBarState(),
        val emailValue: String = "",
        val passwordValue: String = "",
        val fieldStateEmail: EmailFieldState = EmailFieldState.Empty,
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Empty,
        val isLoginBtnEnabled: Boolean = false,
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onLoginClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {},
    )

    @Immutable
    sealed class LoginState {
        object Default : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
    }
}
