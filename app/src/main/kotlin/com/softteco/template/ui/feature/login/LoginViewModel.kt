package com.softteco.template.ui.feature.login

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
import com.softteco.template.data.profile.dto.LoginAuthDto
import com.softteco.template.ui.feature.FieldValidationState
import com.softteco.template.ui.feature.ValidateFields
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel() {

    private val validateFields: ValidateFields = ValidateFields()

    private val loading = MutableStateFlow(false)
    val loginState = MutableStateFlow(false)

    var emailValue by mutableStateOf("")
    var passwordValue by mutableStateOf("")
    fun login(
        userAuth: LoginAuthDto
    ) = viewModelScope.launch {
        loading.value = true
        repository.login(userAuth).run {
            when (this) {
                is Result.Success -> loginState.value = true
                is Result.Error -> loginState.value = false
            }
        }
        loading.value = false
    }


     @OptIn(ExperimentalCoroutinesApi::class)
     private var emailError =
            snapshotFlow { emailValue }
                .mapLatest { validateFields.validateEmail(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MILLIS),
                    initialValue = FieldValidationState()
                )

    val state = combine(
        loading,
        loginState,
        emailError
    ) { loading, loginState, emailError ->
        State(
            loading = loading,
            loginState = loginState,
            emailError = emailError.isEmailCorrect
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    @Immutable
    data class State(
        val loading: Boolean = false,
        val loginState: Boolean = false,
        val emailError: Boolean = false
    )

    fun changeEmail(value: String) {
        this.emailValue = value
    }
}
