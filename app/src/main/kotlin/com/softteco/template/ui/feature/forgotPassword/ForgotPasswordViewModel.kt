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
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.dialog.DialogState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.feature.validateInputValue
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
    private val dialogController: DialogController,
) : ViewModel() {
    private val forgotPasswordState = MutableStateFlow<ScreenState>(ScreenState.Default)
    private var emailState = MutableStateFlow(TextFieldState())
    private val ctaButtonState = MutableStateFlow(false)

    val state = combine(
        forgotPasswordState,
        emailState,
        ctaButtonState,
    ) { forgotPasswordState, email, isCtaEnabled ->
        State(
            screenState = forgotPasswordState,
            email = email,
            onEmailChanged = {
                emailState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onInputComplete = ::onInputComplete,
            isResetBtnEnabled = isCtaEnabled,
            onRestorePasswordClicked = ::onForgotPassword,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch {
            emailState.collect { email ->
                ctaButtonState.value =
                    email.text.validateInputValue(FieldType.EMAIL) is FieldState.Valid
            }
        }
    }

    fun onCreate() {
        forgotPasswordState.value = ScreenState.Default
    }

    private fun onForgotPassword() {
        viewModelScope.launch(appDispatchers.ui) {
            forgotPasswordState.value = ScreenState.Loading

            val email = ResetPasswordDto(email = emailState.value.text)

            val result = repository.resetPassword(email)

            when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.check_email)
                    forgotPasswordState.value = ScreenState.Success
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
                    forgotPasswordState.value = ScreenState.Default
                }
            }
        }
    }

    private fun showSignUpDialog() {
        viewModelScope.launch {
            val dialogState = DialogState(
                titleRes = R.string.dialog_user_not_found_title,
                messageRes = R.string.dialog_user_not_found_message,
                positiveBtnRes = R.string.sign_up,
                positiveBtnAction = {
                    forgotPasswordState.value = ScreenState.Navigate(Screen.SignUp)
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
        val screenState: ScreenState = ScreenState.Default,
        val email: TextFieldState = TextFieldState(),
        val onEmailChanged: (String) -> Unit = {},
        val onInputComplete: () -> Unit = {},
        val isResetBtnEnabled: Boolean = false,
        val onRestorePasswordClicked: () -> Unit = {},
    )
}
