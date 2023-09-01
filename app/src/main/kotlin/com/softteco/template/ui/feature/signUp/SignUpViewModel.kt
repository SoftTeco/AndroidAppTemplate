package com.softteco.template.ui.feature.signUp

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.Constants
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.ui.feature.FieldValidationState
import com.softteco.template.ui.feature.ValidateFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel() {

    private val validateFields: ValidateFields = ValidateFields()

    private val loading = MutableStateFlow(false)
    val signUpState = MutableStateFlow(false)

    var passwordValue by mutableStateOf("")
    var emailValue by mutableStateOf("")
    var firstNameValue by mutableStateOf("")
    var lastNameValue by mutableStateOf("")
    var confirmPasswordValue by mutableStateOf("")
    var birthDayValue by mutableStateOf("")

    fun register(
        user: CreateUserDto
    ) = viewModelScope.launch {
        loading.value = true
        repository.registration(user).run {
            when (this) {
                is Result.Success -> signUpState.value = true
                is Result.Error -> signUpState.value = false
            }
        }
        loading.value = false
    }

    var passwordError =
        snapshotFlow { passwordValue }
            .mapLatest { validateFields.validatePassword(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MILLIS),
                initialValue = FieldValidationState()
            )

    var emailError =
        snapshotFlow { emailValue }
            .mapLatest { validateFields.validateEmail(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MILLIS),
                initialValue = FieldValidationState()
            )

    val state = combine(
        loading,
        signUpState,
        emailError,
        passwordError
    ) { loading, signUpState, emailError, passwordError ->
        State(
            loading = loading,
            signUpState = signUpState,
            emailError = emailError.isEmailCorrect,
            passwordError = passwordError.isPasswordCorrect
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    @Immutable
    data class State(
        val loading: Boolean = false,
        val signUpState: Boolean = false,
        val emailError: Boolean = false,
        val passwordError: Boolean = false
    )

    fun changePassword(value: String) {
        passwordValue = value
    }

    fun changeEmail(value: String) {
        emailValue = value
    }
}
