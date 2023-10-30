package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.SimpleFieldState
import com.softteco.template.ui.feature.ValidateFields.isEmailCorrect
import com.softteco.template.ui.feature.ValidateFields.isHasCapitalizedLetter
import com.softteco.template.ui.feature.ValidateFields.isHasMinimum
import com.softteco.template.utils.combine
import com.softteco.template.utils.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel() {
    private val registrationState = MutableStateFlow<SignupState>(SignupState.Default)
    private var userNameStateValue = MutableStateFlow("")
    private var emailStateValue = MutableStateFlow("")
    private var passwordStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val emailFieldState =
        MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

    val state = combine(
        registrationState,
        userNameStateValue,
        emailStateValue,
        passwordStateValue,
        snackBarState,
        emailFieldState
    ) { registrationState, userName, emailValue, passwordValue, snackBar, emailState ->
        State(
            registrationState = registrationState,
            userNameValue = userName,
            emailValue = emailValue,
            passwordValue = passwordValue,
            fieldStateUserName = when {
                userName.isEmpty() -> SimpleFieldState.Empty
                else -> SimpleFieldState.Success
            },
            fieldStateEmail = emailState,
            fieldStatePassword = when {
                passwordValue.isEmpty() -> PasswordFieldState.Empty
                else -> PasswordFieldState.Success
            },
            isPasswordHasMinimum = passwordValue.isHasMinimum(),
            isPasswordHasUpperCase = passwordValue.isHasCapitalizedLetter(),
            snackBar = snackBar,
            dismissSnackBar = { snackBarState.value = SnackBarState() },
            onUserNameChanged = { userNameStateValue.value = it },
            onEmailChanged = {
                emailStateValue.value = it.trim()
                validateEmail(it)
            },
            onPasswordChanged = { passwordStateValue.value = it },
            onRegisterClicked = ::onRegister
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

    private fun onRegister() {
        val isAllFieldsValid = state.value.run {
            fieldStateEmail is EmailFieldState.Success &&
                fieldStatePassword is PasswordFieldState.Success &&
                fieldStateUserName is SimpleFieldState.Success &&
                isPasswordHasUpperCase && isPasswordHasMinimum
        }
        if (isAllFieldsValid) {
            viewModelScope.launch {
                registrationState.value = SignupState.Loading
                val createUserDto = CreateUserDto(
                    firstName = userNameStateValue.value,
                    email = emailStateValue.value,
                    password = passwordStateValue.value,
                    confirmPassword = passwordStateValue.value,
                )
                when (val result = repository.registration(createUserDto)) {
                    is Result.Success -> registrationState.value = SignupState.Success
                    is Result.Error -> handleApiError(result, snackBarState)
                }
                registrationState.value = SignupState.Default
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
        val registrationState: SignupState = SignupState.Default,
        val snackBar: SnackBarState = SnackBarState(),
        val userNameValue: String = "",
        val emailValue: String = "",
        val passwordValue: String = "",
        val fieldStateUserName: SimpleFieldState = SimpleFieldState.Waiting,
        val fieldStateEmail: EmailFieldState = EmailFieldState.Waiting,
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Waiting,
        val isPasswordHasMinimum: Boolean = false,
        val isPasswordHasUpperCase: Boolean = false,
        val onUserNameChanged: (String) -> Unit = {},
        val onEmailChanged: (String) -> Unit = {},
        val onPasswordChanged: (String) -> Unit = {},
        val onRegisterClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {}
    )

    @Immutable
    sealed class SignupState {
        object Default : SignupState()
        object Loading : SignupState()
        object Success : SignupState()
    }
}
