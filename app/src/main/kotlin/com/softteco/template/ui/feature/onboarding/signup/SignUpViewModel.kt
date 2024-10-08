package com.softteco.template.ui.feature.onboarding.signup

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.auth.dto.CreateUserDto
import com.softteco.template.data.auth.repository.AuthRepository
import com.softteco.template.data.base.error.AppError.AuthError.InvalidEmail
import com.softteco.template.data.base.error.AppError.AuthError.InvalidUsername
import com.softteco.template.data.base.error.Result
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldState.EmailError
import com.softteco.template.ui.components.FieldState.UsernameError
import com.softteco.template.ui.components.FieldState.Valid
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {
    private val signUpState = MutableStateFlow(SignupState())

    private val usernameState = MutableStateFlow(TextFieldState())
    private val emailState = MutableStateFlow(TextFieldState())
    private val passwordState = MutableStateFlow(TextFieldState())

    private var isTermsAccepted = MutableStateFlow(false)

    private val _navDestination = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navDestination = _navDestination.asSharedFlow().distinctUntilChanged()

    val state = combine(
        signUpState,
        usernameState,
        emailState,
        passwordState,
        isTermsAccepted,
    ) { signUpState, username, email, password, terms ->
        State(
            signUpState = signUpState,
            username = username,
            email = email,
            password = password,
            onUsernameChanged = {
                usernameState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onEmailChanged = {
                emailState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onPasswordChanged = {
                passwordState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onInputComplete = { onInputComplete(it) },
            isTermsAccepted = terms,
            onCheckTermsChange = { isTermsAccepted.value = it },
            isSignupBtnEnabled = !signUpState.loading && signUpState.isCtaEnabled,
            onRegisterClicked = ::onRegister,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch {
            combine(
                usernameState,
                emailState,
                passwordState,
                isTermsAccepted,
            ) { username, email, password, isTermsAccepted ->
                val isCtaEnabled = username.text.validateInputValue(FieldType.USERNAME) is Valid &&
                    email.text.validateInputValue(FieldType.EMAIL) is Valid &&
                    password.text.validateInputValue(FieldType.PASSWORD) is Valid &&
                    isTermsAccepted
                signUpState.update { SignupState(it.loading, isCtaEnabled) }
            }.collect()
        }
    }

    private fun onRegister() {
        viewModelScope.launch(appDispatchers.ui) {
            signUpState.update { SignupState(true, it.isCtaEnabled) }

            val createUserDto = CreateUserDto(
                username = usernameState.value.text,
                email = emailState.value.text,
                password = passwordState.value.text,
            )

            val result = authRepository.registration(createUserDto)

            when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.success_signup)
                    _navDestination.tryEmit(Screen.Login)
                }

                is Result.Error -> {
                    when (result.error) {
                        InvalidUsername -> {
                            usernameState.update {
                                TextFieldState(
                                    it.text,
                                    EmailError(R.string.username_not_valid)
                                )
                            }
                        }

                        InvalidEmail -> {
                            emailState.update {
                                TextFieldState(
                                    it.text,
                                    UsernameError(R.string.email_not_valid)
                                )
                            }
                        }

                        else -> { /*NOOP*/
                        }
                    }
                    snackbarController.showSnackbar(result.error.messageRes)
                }
            }
            signUpState.update { SignupState(false, it.isCtaEnabled) }
        }
    }

    private fun onInputComplete(fieldType: FieldType) {
        when (fieldType) {
            FieldType.EMAIL -> emailState
            FieldType.PASSWORD -> passwordState
            FieldType.USERNAME -> usernameState
        }.update { TextFieldState(it.text, it.text.validateInputValue(fieldType)) }
    }

    @Immutable
    data class State(
        val signUpState: SignupState = SignupState(),
        val username: TextFieldState = TextFieldState(),
        val email: TextFieldState = TextFieldState(),
        val password: TextFieldState = TextFieldState(),
        val onUsernameChanged: (String) -> Unit = {},
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onInputComplete: (FieldType) -> Unit = {},
        val isTermsAccepted: Boolean = false,
        val isSignupBtnEnabled: Boolean = false,
        val onRegisterClicked: () -> Unit = {},
        val onCheckTermsChange: (Boolean) -> Unit = {},
    )

    @Immutable
    data class SignupState(
        val loading: Boolean = false,
        val isCtaEnabled: Boolean = false,
    )
}
