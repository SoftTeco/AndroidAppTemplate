package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.AppError.AuthError.InvalidEmail
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldState.EmailError
import com.softteco.template.ui.components.FieldState.Valid
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.validateInputValue
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {
    private val registrationState = MutableStateFlow<SignupState>(SignupState.Default())

    private val usernameState = MutableStateFlow(TextFieldState())
    private val emailState = MutableStateFlow(TextFieldState())
    private val passwordState = MutableStateFlow(TextFieldState())

    private var isTermsAccepted = MutableStateFlow(false)

    val state = combine(
        registrationState,
        usernameState,
        emailState,
        passwordState,
        isTermsAccepted,
    ) { registrationState, username, email, password, terms ->
        State(
            registrationState = registrationState,
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
            isSignupBtnEnabled = registrationState is SignupState.Default && registrationState.isCtaEnabled,
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
                registrationState.value = SignupState.Default(isCtaEnabled)
            }.collect()
        }
    }

    private fun onRegister() {
        viewModelScope.launch(appDispatchers.ui) {
            registrationState.value = SignupState.Loading

            val createUserDto = CreateUserDto(
                username = usernameState.value.text,
                email = emailState.value.text,
                password = passwordState.value.text,
            )

            val result = repository.registration(createUserDto)
            registrationState.value = when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.success)
                    SignupState.Success(result.data)
                }

                is Result.Error -> {
                    if (result.error == InvalidEmail) {
                        usernameState.update {
                            TextFieldState(
                                it.text,
                                EmailError(R.string.email_not_valid)
                            )
                        }
                    }
                    snackbarController.showSnackbar(result.error.messageRes)
                    SignupState.Default(true)
                }
            }
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
        val registrationState: SignupState = SignupState.Default(),
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
    sealed class SignupState {
        data class Default(val isCtaEnabled: Boolean = false) : SignupState()
        data object Loading : SignupState()
        class Success(val email: String) : SignupState()
    }
}
