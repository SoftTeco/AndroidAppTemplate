package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.AppError.AuthError.InvalidEmail
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.TextFieldState.EmailError
import com.softteco.template.ui.components.TextFieldState.Valid
import com.softteco.template.ui.components.snackbar.SnackbarController
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
class SignUpViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {
    private val registrationState = MutableStateFlow<SignupState>(SignupState.Default())

    private var usernameValue = MutableStateFlow("")
    private val usernameFieldState = MutableStateFlow<TextFieldState>(TextFieldState.Empty)

    private var emailValue = MutableStateFlow("")
    private val emailFieldState = MutableStateFlow<TextFieldState>(TextFieldState.Empty)

    private var passwordValue = MutableStateFlow("")
    private val passwordFieldState = MutableStateFlow<TextFieldState>(TextFieldState.Empty)

    private var isTermsAccepted = MutableStateFlow(false)

    val state = combine(
        registrationState,
        usernameValue,
        usernameFieldState,
        emailValue,
        emailFieldState,
        passwordValue,
        passwordFieldState,
    ) { registrationState, username, usernameState, email, emailState, password, passwordState ->
        State(
            registrationState = registrationState,
            username = username,
            email = email,
            password = password,
            usernameFieldState = usernameState,
            emailFieldState = emailState,
            passwordFieldState = passwordState,
            onUsernameChanged = {
                usernameValue.value = it.trim()
                usernameFieldState.value = TextFieldState.AwaitingInput
            },
            onEmailChanged = {
                viewModelScope.launch {
                    emailValue.value = it.trim()
                    emailFieldState.value = TextFieldState.AwaitingInput
                }
            },
            onPasswordChanged = {
                passwordValue.value = it.trim()
                passwordFieldState.value = TextFieldState.AwaitingInput
            },
            onInputComplete = { onInputComplete(it) },
            isTermsAccepted = isTermsAccepted.value,
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
                usernameValue,
                emailValue,
                passwordValue,
                isTermsAccepted,
            ) { username, email, password, isTermsAccepted ->
                val isCtaEnabled = username.validateInputValue(FieldType.USERNAME) is Valid &&
                    email.validateInputValue(FieldType.EMAIL) is Valid &&
                    password.validateInputValue(FieldType.PASSWORD) is Valid &&
                    isTermsAccepted
                registrationState.value = SignupState.Default(isCtaEnabled)
            }.collect()
        }
    }

    private fun onRegister() {
        viewModelScope.launch(appDispatchers.ui) {
            registrationState.value = SignupState.Loading

            val createUserDto = CreateUserDto(
                username = usernameValue.value,
                email = emailValue.value,
                password = passwordValue.value,
            )

            val result = repository.registration(createUserDto)
            registrationState.value = when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.success)
                    SignupState.Success(result.data)
                }

                is Result.Error -> {
                    if (result.error == InvalidEmail) {
                        usernameFieldState.value = EmailError(R.string.email_not_valid)
                    }
                    snackbarController.showSnackbar(result.error.messageRes)
                    SignupState.Default(true)
                }
            }
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

            FieldType.USERNAME -> {
                usernameFieldState.value = usernameValue.value.validateInputValue(fieldType)
            }
        }
    }

    @Immutable
    data class State(
        val registrationState: SignupState = SignupState.Default(),
        val username: String = "",
        val email: String = "",
        val password: String = "",
        val usernameFieldState: TextFieldState = TextFieldState.Empty,
        val emailFieldState: TextFieldState = TextFieldState.Empty,
        val passwordFieldState: TextFieldState = TextFieldState.Empty,
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
