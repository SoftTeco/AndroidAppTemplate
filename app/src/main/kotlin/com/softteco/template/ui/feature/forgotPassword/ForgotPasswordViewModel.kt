package com.softteco.template.ui.feature.forgotPassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.ui.components.snackBar.SnackBarState
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.validateEmail
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers
) : ViewModel() {
    private val forgotPasswordState =
        MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Default)
    private var emailStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val emailFieldState =
        MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

    val state = combine(
        forgotPasswordState,
        emailStateValue,
        snackBarState,
        emailFieldState
    ) { forgotPasswordState, emailValue, snackBar, emailState ->
        State(
            forgotPasswordState = forgotPasswordState,
            emailValue = emailValue,
            snackBar = snackBar,
            fieldStateEmail = emailState,
            isResetBtnEnabled = emailState is EmailFieldState.Success,
            dismissSnackBar = { snackBarState.value = SnackBarState() },
            onEmailChanged = {
                emailStateValue.value = it.trim()
                validateEmail(emailStateValue, emailFieldState, viewModelScope, appDispatchers)
            },
            onRestorePasswordClicked = ::onForgotPassword,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    private fun handleSuccess() {
        snackBarState.value = SnackBarState(
            textId = R.string.check_email,
            show = true,
        )
    }

    private fun onForgotPassword() {
        if (state.value.fieldStateEmail is EmailFieldState.Success) {
            viewModelScope.launch(appDispatchers.ui) {
                forgotPasswordState.value = ForgotPasswordState.Loading

                val email = ResetPasswordDto(email = emailStateValue.value)

                val result = repository.resetPassword(email)
                forgotPasswordState.value = when (result) {
                    is Result.Success -> {
                        handleSuccess()
                        ForgotPasswordState.Success
                    }

                    is Result.Error -> {
                        handleApiError(result, snackBarState)
                        ForgotPasswordState.Default
                    }
                }
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
        val forgotPasswordState: ForgotPasswordState = ForgotPasswordState.Default,
        val emailValue: String = "",
        val fieldStateEmail: EmailFieldState = EmailFieldState.Empty,
        val snackBar: SnackBarState = SnackBarState(),
        val isResetBtnEnabled: Boolean = false,
        val onEmailChanged: (String) -> Unit = {},
        val onRestorePasswordClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {},
    )

    @Immutable
    sealed class ForgotPasswordState {
        object Default : ForgotPasswordState()
        object Loading : ForgotPasswordState()
        object Success : ForgotPasswordState()
    }
}
