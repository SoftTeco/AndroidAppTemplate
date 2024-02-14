package com.softteco.template.ui.feature.login

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.AppError.AuthError.EmailNotExist
import com.softteco.template.data.base.error.AppError.AuthError.WrongCredentials
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.dialog.DialogState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.feature.validateEmail
import com.softteco.template.utils.AppDispatchers
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
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
    private val dialogController: DialogController,
) : ViewModel() {

    private val loginState = MutableStateFlow<ScreenState>(ScreenState.Default)
    private var emailStateValue = MutableStateFlow("")
    private var passwordStateValue = MutableStateFlow("")
    private val emailFieldState = MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

    val state = combine(
        loginState,
        emailStateValue,
        passwordStateValue,
        emailFieldState
    ) { loginState, emailValue, passwordValue, emailState ->
        State(
            screenState = loginState,
            emailValue = emailValue,
            passwordValue = passwordValue,
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
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    fun onCreate() {
        loginState.value = ScreenState.Default
    }

    private fun onLogin() {
        viewModelScope.launch(appDispatchers.io) {
            loginState.value = ScreenState.Loading

            val credentials = CredentialsDto(
                email = emailStateValue.value,
                password = passwordStateValue.value
            )

            val result = repository.login(credentials)

            when (result) {
                is Result.Success -> loginState.value = ScreenState.Success

                is Result.Error -> {
                    when (result.error) {
                        EmailNotExist, WrongCredentials -> {
                            emailFieldState.value = EmailFieldState.Error

                            if (result.error == EmailNotExist) {
                                showSignUpDialog()
                            } else {
                                snackbarController.showSnackbar(result.error.messageRes)
                            }
                        }

                        else -> snackbarController.showSnackbar(result.error.messageRes)
                    }
                    loginState.value = ScreenState.Default
                }
            }
        }
    }

    private fun showSignUpDialog() {
        viewModelScope.launch {
            val dialogState = DialogState(
                titleRes = R.string.dialog_user_not_found_title,
                messageRes = R.string.dialog_user_not_found_message,
                positiveBtnRes = R.string.sign_up,
                positiveBtnAction = { loginState.value = ScreenState.Navigate(Screen.SignUp) },
                negativeBtnRes = R.string.cancel,
            )
            dialogController.showDialog(dialogState)
        }
    }

    @Immutable
    data class State(
        val screenState: ScreenState = ScreenState.Default,
        val emailValue: String = "",
        val passwordValue: String = "",
        val fieldStateEmail: EmailFieldState = EmailFieldState.Empty,
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Empty,
        val isLoginBtnEnabled: Boolean = false,
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onLoginClicked: () -> Unit = {},
    )
}
