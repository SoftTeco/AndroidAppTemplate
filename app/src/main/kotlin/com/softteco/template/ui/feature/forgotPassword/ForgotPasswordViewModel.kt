package com.softteco.template.ui.feature.forgotPassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.AppError.AuthError.EmailNotExist
import com.softteco.template.data.base.error.AppError.AuthError.InvalidEmail
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.EmailFieldState
import com.softteco.template.ui.feature.validateEmail
import com.softteco.template.utils.AppDispatchers
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
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {
    private val forgotPasswordState =
        MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Default)
    private var emailStateValue = MutableStateFlow("")
    private val emailFieldState =
        MutableStateFlow<EmailFieldState>(EmailFieldState.Empty)

    val state = combine(
        forgotPasswordState,
        emailStateValue,
        emailFieldState
    ) { forgotPasswordState, emailValue, emailState ->
        State(
            forgotPasswordState = forgotPasswordState,
            emailValue = emailValue,
            fieldStateEmail = emailState,
            isResetBtnEnabled = emailState is EmailFieldState.Success,
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

    private fun onForgotPassword() {
        if (state.value.fieldStateEmail is EmailFieldState.Success) {
            viewModelScope.launch(appDispatchers.ui) {
                forgotPasswordState.value = ForgotPasswordState.Loading

                val email = ResetPasswordDto(email = emailStateValue.value)

                val result = repository.resetPassword(email)
                forgotPasswordState.value = when (result) {
                    is Result.Success -> {
                        snackbarController.showSnackbar(R.string.check_email)
                        ForgotPasswordState.Success
                    }

                    is Result.Error -> {
                        // TODO Add a dialog asking to register (with an option to go to "Sign up")
                        if (result.error == EmailNotExist || result.error == InvalidEmail) {
                            emailFieldState.value = EmailFieldState.Error
                        }
                        snackbarController.showSnackbar(R.string.check_email)
                        ForgotPasswordState.Default
                    }
                }
            }
        } else {
            snackbarController.showSnackbar(R.string.empty_fields_error)
        }
    }

    @Immutable
    data class State(
        val forgotPasswordState: ForgotPasswordState = ForgotPasswordState.Default,
        val emailValue: String = "",
        val fieldStateEmail: EmailFieldState = EmailFieldState.Empty,
        val isResetBtnEnabled: Boolean = false,
        val onEmailChanged: (String) -> Unit = {},
        val onRestorePasswordClicked: () -> Unit = {},
    )

    @Immutable
    sealed class ForgotPasswordState {
        object Default : ForgotPasswordState()
        object Loading : ForgotPasswordState()
        object Success : ForgotPasswordState()
    }
}
