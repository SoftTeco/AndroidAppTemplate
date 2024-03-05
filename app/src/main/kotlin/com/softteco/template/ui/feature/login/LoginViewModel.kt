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
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.dialog.DialogState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.validateInputValue
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
    private val dialogController: DialogController,
) : ViewModel() {

    private val loading = MutableStateFlow(false)
    private var emailState = MutableStateFlow(TextFieldState())
    private var passwordState = MutableStateFlow(TextFieldState())
    private val ctaButtonState = MutableStateFlow(false)

    private val _navDestination = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navDestination = _navDestination.asSharedFlow()

    val state = combine(
        loading,
        emailState,
        passwordState,
        ctaButtonState,
    ) { loading, email, password, isCtaEnabled ->
        State(
            loading = loading,
            email = email,
            password = password,
            onEmailChanged = {
                emailState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onPasswordChanged = {
                passwordState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            isLoginBtnEnabled = !loading && isCtaEnabled,
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
            combine(emailState, passwordState) { email, password ->
                ctaButtonState.value =
                    email.text.validateInputValue(FieldType.EMAIL) is FieldState.Valid &&
                    password.text.validateInputValue(FieldType.PASSWORD) is FieldState.Valid
            }.collect()
        }
    }

    private fun onLogin() {
        viewModelScope.launch(appDispatchers.io) {
            loading.value = true

            val credentials = CredentialsDto(
                email = emailState.value.text,
                password = passwordState.value.text,
            )

            val result = repository.login(credentials)

            when (result) {
                is Result.Success -> _navDestination.tryEmit(Screen.Home)

                is Result.Error -> {
                    when (result.error) {
                        EmailNotExist, WrongCredentials -> {
                            emailState.update {
                                TextFieldState(
                                    it.text,
                                    FieldState.EmailError(R.string.email_not_valid)
                                )
                            }

                            if (result.error == EmailNotExist) {
                                showSignUpDialog()
                            } else {
                                snackbarController.showSnackbar(result.error.messageRes)
                            }
                        }

                        else -> snackbarController.showSnackbar(result.error.messageRes)
                    }
                    loading.value = false
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
                    _navDestination.tryEmit(Screen.SignUp)
                },
                negativeBtnRes = R.string.cancel,
            )
            dialogController.showDialog(dialogState)
        }
    }

    private fun onInputComplete(fieldType: FieldType) {
        when (fieldType) {
            FieldType.EMAIL -> emailState.update {
                TextFieldState(it.text, it.text.validateInputValue(fieldType))
            }

            FieldType.PASSWORD -> passwordState.update {
                TextFieldState(it.text, it.text.validateInputValue(fieldType))
            }

            FieldType.USERNAME -> { /*NOOP*/
            }
        }
    }

    @Immutable
    data class State(
        val loading: Boolean = false,
        val email: TextFieldState = TextFieldState(),
        val password: TextFieldState = TextFieldState(),
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onInputComplete: (FieldType) -> Unit = {},
        val isLoginBtnEnabled: Boolean = false,
        val onLoginClicked: () -> Unit = {},
    )
}
