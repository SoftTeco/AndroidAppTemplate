package com.softteco.template.ui.feature.login

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.ValidateFields.isEmailCorrect
import com.softteco.template.utils.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel() {

    private val loginState = MutableStateFlow<LoginState>(LoginState.Default)
    private var emailStateValue = MutableStateFlow("")
    private var passwordStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val emailFieldState =
        MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

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
            dismissSnackBar = { snackBarState.value = SnackBarState() },
            onEmailChanged = {
                emailStateValue.value = it.trim()
                validateEmail(it)
            },
            onPasswordChanged = { passwordStateValue.value = it },
            fieldStateEmail = emailState,
            fieldStatePassword = when {
                passwordStateValue.value.isEmpty() -> PasswordFieldState.Empty
                else -> PasswordFieldState.Success
            },
            onLoginClicked = ::onLogin,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    private fun validateEmail(emailValue: String) {
        viewModelScope.launch {
            emailFieldState.value = EmailFieldState.Waiting
            delay(1.seconds)
            emailFieldState.value = when {
                emailValue.isEmailCorrect() -> EmailFieldState.Success
                emailValue.isEmpty() -> EmailFieldState.Empty
                else -> EmailFieldState.Error
            }
        }
    }

    private fun onLogin() {
        val isAllFieldsValid = state.value.run {
            fieldStateEmail is EmailFieldState.Success &&
                fieldStatePassword is PasswordFieldState.Success
        }
        if (isAllFieldsValid) {
            viewModelScope.launch {
                loginState.value = LoginState.Loading

                val credentials = CredentialsDto(
                    email = emailStateValue.value,
                    password = passwordStateValue.value
                )

                val result = repository.login(credentials)
                loginState.value = when (result) {
                    is Result.Success -> {
                        snackBarState.value = SnackBarState(R.string.success, true)
                        LoginState.Success
                    }

                    is Result.Error -> {
                        handleApiError(result, snackBarState)
                        LoginState.Default
                    }
                }
            }
        } else {
            snackBarState.value = SnackBarState(
                R.string.empty_fields_error,
                true
            )
        }
    }

    @Immutable
    data class State(
        val loginState: LoginState = LoginState.Default,
        val snackBar: SnackBarState = SnackBarState(),
        val emailValue: String = "",
        val passwordValue: String = "",
        val fieldStateEmail: EmailFieldState = EmailFieldState.Waiting,
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Waiting,
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onLoginClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {}
    )

    @Immutable
    sealed class LoginState {
        object Default : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
    }
}
