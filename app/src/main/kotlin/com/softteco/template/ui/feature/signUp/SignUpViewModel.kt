package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.AppError.AuthError.InvalidEmail
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.validateEmail
import com.softteco.template.ui.feature.validatePassword
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {
    private val registrationState = MutableStateFlow<SignupState>(SignupState.Default)
    private var userNameStateValue = MutableStateFlow("")
    private var emailStateValue = MutableStateFlow("")
    private var passwordStateValue = MutableStateFlow("")
    private val emailFieldState = MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)
    private var termsCheckedStateValue = MutableStateFlow(false)

    val state = combine(
        registrationState,
        userNameStateValue,
        emailStateValue,
        passwordStateValue,
        emailFieldState,
        termsCheckedStateValue,
    ) { registrationState, userName, emailValue, passwordValue, emailState, termsCheckedState ->
        val passwordState = validatePassword(passwordValue)
        State(
            registrationState = registrationState,
            userNameValue = userName,
            emailValue = emailValue,
            passwordValue = passwordValue,
            fieldStateEmail = emailState,
            fieldStatePassword = passwordState,
            termsCheckedStateValue = termsCheckedState,
            isSignupBtnEnabled = emailState is EmailFieldState.Success &&
                passwordState is PasswordFieldState.Success &&
                userName.isNotEmpty() &&
                termsCheckedState,
            onUserNameChanged = { userNameStateValue.value = it.trim() },
            onEmailChanged = {
                emailStateValue.value = it.trim()
                validateEmail(emailStateValue, emailFieldState, viewModelScope, appDispatchers)
            },
            onPasswordChanged = { passwordStateValue.value = it.trim() },
            onCheckTermsChange = { termsCheckedStateValue.value = it },
            onRegisterClicked = ::onRegister,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    private fun onRegister() {
        viewModelScope.launch(appDispatchers.ui) {
            registrationState.value = SignupState.Loading

            val createUserDto = CreateUserDto(
                username = userNameStateValue.value,
                email = emailStateValue.value,
                password = passwordStateValue.value,
            )

            val result = repository.registration(createUserDto)
            registrationState.value = when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.success)
                    SignupState.Success(result.data)
                }

                is Result.Error -> {
                    if (result.error == InvalidEmail) {
                        emailFieldState.value = EmailFieldState.Error
                    }
                    snackbarController.showSnackbar(result.error.messageRes)
                    SignupState.Default
                }
            }
        }
    }

    @Immutable
    data class State(
        val registrationState: SignupState = SignupState.Default,
        val userNameValue: String = "",
        val emailValue: String = "",
        val passwordValue: String = "",
        val termsCheckedStateValue: Boolean = false,
        val fieldStateEmail: EmailFieldState = EmailFieldState.Empty,
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Empty,
        val isSignupBtnEnabled: Boolean = false,
        val onUserNameChanged: (String) -> Unit = {},
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onRegisterClicked: () -> Unit = {},
        val onCheckTermsChange: (Boolean) -> Unit = {},
    )

    @Immutable
    sealed class SignupState {
        object Default : SignupState()
        object Loading : SignupState()
        class Success(val email: String) : SignupState()
    }
}
