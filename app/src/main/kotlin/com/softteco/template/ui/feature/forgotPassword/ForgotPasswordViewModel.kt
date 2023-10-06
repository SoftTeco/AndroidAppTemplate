package com.softteco.template.ui.feature.forgotPassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.ForgotPasswordDto
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.ValidateFields.isEmailCorrect
import com.softteco.template.utils.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : ViewModel() {

    private val loading = MutableStateFlow(false)
    private val forgotPasswordState = MutableStateFlow(false)
    private var emailStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val emailFieldState =
        MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

    val state = combine(
        loading,
        emailStateValue,
        snackBarState,
        emailFieldState
    ) { loading, emailValue, snackBar, emailState ->
        State(
            loading = loading,
            emailValue = emailValue,
            snackBar = snackBar,
            dismissSnackBar = { snackBarState.value = SnackBarState() },
            onEmailChanged = {
                emailStateValue.value = it.trim()
                validateEmail(it)
            },
            fieldStateEmail = emailState,
            onRestorePasswordClicked = ::onForgotPassword,
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

    private fun handleSuccess() {
        forgotPasswordState.value = true
        snackBarState.value = SnackBarState(
            textId = R.string.check_email,
            show = true,
        )
    }

    private fun onForgotPassword() {
        if (state.value.fieldStateEmail is EmailFieldState.Success) {
            viewModelScope.launch {
                loading.value = true
                val forgotPassword = ForgotPasswordDto(
                    email = emailStateValue.value
                )
                when (val result = repository.restorePassword(forgotPassword)) {
                    is Result.Success -> handleSuccess()
                    is Result.Error -> handleApiError(result, snackBarState)
                }
                loading.value = false
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
        val loading: Boolean = false,
        val forgotPasswordState: Boolean = false,
        val emailValue: String = "",
        val fieldStateEmail: EmailFieldState = EmailFieldState.Waiting,
        val snackBar: SnackBarState = SnackBarState(),
        val onEmailChanged: (String) -> Unit = {},
        val onRestorePasswordClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {}
    )
}
