package com.softteco.template.ui.feature.onboarding.password.forgot

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.auth.dto.ResetPasswordDto
import com.softteco.template.data.auth.repository.AuthRepository
import com.softteco.template.data.base.error.AppError.AuthError.EmailNotExist
import com.softteco.template.data.base.error.AppError.AuthError.InvalidEmail
import com.softteco.template.data.base.error.Result
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.dialog.DialogState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.validateInputValue
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
    private val dialogController: DialogController,
) : ViewModel() {
    private val loading = MutableStateFlow(false)
    private var emailState = MutableStateFlow(TextFieldState())
    private val ctaButtonState = emailState.map { email ->
        email.text.validateInputValue(FieldType.EMAIL) is FieldState.Valid
    }

    private val _navDestination = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navDestination = _navDestination.asSharedFlow().distinctUntilChanged()

    val state = combine(
        loading,
        emailState,
        ctaButtonState,
    ) { loading, email, isCtaEnabled ->
        State(
            loading = loading,
            email = email,
            onEmailChanged = {
                emailState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onInputComplete = ::onInputComplete,
            isResetBtnEnabled = !loading && isCtaEnabled,
            onRestorePasswordClicked = ::onForgotPassword,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    private fun onForgotPassword() {
        viewModelScope.launch(appDispatchers.ui) {
            loading.value = true

            val email = ResetPasswordDto(email = emailState.value.text)

            val result = authRepository.resetPassword(email)

            when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.check_email)
                    _navDestination.tryEmit(Screen.Login)
                }

                is Result.Error -> {
                    when (result.error) {
                        EmailNotExist, InvalidEmail -> {
                            emailState.update {
                                TextFieldState(
                                    it.text,
                                    FieldState.EmailError(R.string.email_not_valid)
                                )
                            }

                            if (result.error == EmailNotExist) {
                                showSignUpDialog()
                            } else {
                                snackbarController.showSnackbar(result.error.messageRes)
                            }
                        }

                        else -> snackbarController.showSnackbar(result.error.messageRes)
                    }
                }
            }

            loading.value = false
        }
    }

    private fun showSignUpDialog() {
        viewModelScope.launch {
            val dialogState = DialogState(
                titleRes = R.string.dialog_user_not_found_title,
                messageRes = R.string.dialog_user_not_found_message,
                positiveBtnRes = R.string.sign_up,
                positiveBtnAction = {
                    _navDestination.tryEmit(Screen.SignUp)
                },
                negativeBtnRes = R.string.cancel,
            )
            dialogController.showDialog(dialogState)
        }
    }

    private fun onInputComplete() {
        emailState.update { TextFieldState(it.text, it.text.validateInputValue(FieldType.EMAIL)) }
    }

    @Immutable
    data class State(
        val loading: Boolean = false,
        val email: TextFieldState = TextFieldState(),
        val onEmailChanged: (String) -> Unit = {},
        val onInputComplete: () -> Unit = {},
        val isResetBtnEnabled: Boolean = false,
        val onRestorePasswordClicked: () -> Unit = {},
    )
}
