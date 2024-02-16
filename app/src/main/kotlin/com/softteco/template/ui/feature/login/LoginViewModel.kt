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
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.dialog.DialogState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.feature.validateInputValue
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
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
    private var emailValue = MutableStateFlow("")
    private val emailFieldState = MutableStateFlow<TextFieldState>(TextFieldState.Empty)
    private var passwordValue = MutableStateFlow("")
    private val passwordFieldState = MutableStateFlow<TextFieldState>(TextFieldState.Empty)
    private val ctaButtonState = MutableStateFlow(false)

    val state = combine(
        loginState,
        emailValue,
        emailFieldState,
        passwordValue,
        passwordFieldState,
        ctaButtonState,
    ) { loginState, email, emailState, password, passwordState, isCtaEnabled ->
        State(
            screenState = loginState,
            email = email,
            password = password,
            emailFieldState = emailState,
            passwordFieldState = passwordState,
            onEmailChanged = {
                emailValue.value = it.trim()
                emailFieldState.value = TextFieldState.AwaitingInput
            },
            onPasswordChanged = {
                passwordValue.value = it.trim()
                passwordFieldState.value = TextFieldState.AwaitingInput
            },
            isLoginBtnEnabled = isCtaEnabled,
            onInputComplete = { onInputComplete(it) },
            onLoginClicked = ::onLogin,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch {
            combine(emailValue, passwordValue) { email, password ->
                ctaButtonState.value =
                    email.validateInputValue(FieldType.EMAIL) is TextFieldState.Valid &&
                    password.validateInputValue(FieldType.PASSWORD) is TextFieldState.Valid
            }.collect()
        }
    }

    fun onCreate() {
        loginState.value = ScreenState.Default
    }

    private fun onLogin() {
        viewModelScope.launch(appDispatchers.io) {
            loginState.value = ScreenState.Loading

            val credentials = CredentialsDto(
                email = emailValue.value,
                password = passwordValue.value
            )

            val result = repository.login(credentials)

            when (result) {
                is Result.Success -> loginState.value = ScreenState.Success

                is Result.Error -> {
                    when (result.error) {
                        EmailNotExist, WrongCredentials -> {
                            emailFieldState.value =
                                TextFieldState.EmailError(R.string.email_not_valid)

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
                positiveBtnAction = {
                    loginState.value = ScreenState.Navigate(Screen.SignUp)
                },
                negativeBtnRes = R.string.cancel,
            )
            dialogController.showDialog(dialogState)
        }
    }

    private fun onInputComplete(fieldType: FieldType) {
        when (fieldType) {
            FieldType.EMAIL -> {
                emailFieldState.value = emailValue.value.validateInputValue(fieldType)
            }

            FieldType.PASSWORD -> {
                passwordFieldState.value = passwordValue.value.validateInputValue(fieldType)
            }

            FieldType.USERNAME -> { /*NOOP*/
            }
        }
    }

    @Immutable
    data class State(
        val screenState: ScreenState = ScreenState.Default,
        val email: String = "",
        val password: String = "",
        val emailFieldState: TextFieldState = TextFieldState.Empty,
        val passwordFieldState: TextFieldState = TextFieldState.Empty,
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onInputComplete: (FieldType) -> Unit = {},
        val isLoginBtnEnabled: Boolean = false,
        val onLoginClicked: () -> Unit = {},
    )
}
