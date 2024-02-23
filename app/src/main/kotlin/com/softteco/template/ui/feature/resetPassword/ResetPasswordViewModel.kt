package com.softteco.template.ui.feature.resetPassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.NewPasswordDto
import com.softteco.template.navigation.AppNavHost
import com.softteco.template.ui.components.FieldState
import com.softteco.template.ui.components.FieldState.Valid
import com.softteco.template.ui.components.FieldType
import com.softteco.template.ui.components.TextFieldState
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
class ResetPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {

    private val resetPasswordState = MutableStateFlow<ScreenState>(ScreenState.Default)
    private var passwordState = MutableStateFlow(TextFieldState())
    private val token: String = checkNotNull(savedStateHandle[AppNavHost.RESET_TOKEN_ARG])
    private val ctaButtonState = MutableStateFlow(false)

    val state = combine(
        resetPasswordState,
        passwordState,
        ctaButtonState,
    ) { resetPasswordState, password, isCtaEnabled ->
        State(
            resetPasswordState = resetPasswordState,
            password = password,
            onPasswordChanged = {
                passwordState.value = TextFieldState(it, FieldState.AwaitingInput)
            },
            onInputComplete = ::onInputComplete,
            isResetBtnEnabled = isCtaEnabled,
            onResetPasswordClicked = ::onResetPassword
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch {
            passwordState.collect { password ->
                ctaButtonState.value = password.text.validateInputValue(FieldType.PASSWORD) is Valid
            }
        }
    }

    private fun onResetPassword() {
        viewModelScope.launch(appDispatchers.ui) {
            resetPasswordState.value = ScreenState.Loading

            val newPassword = NewPasswordDto(
                password = passwordState.value.text,
                confirmation = passwordState.value.text,
            )

            val result = repository.changePassword(token, newPassword)

            when (result) {
                is Result.Success -> {
                    snackbarController.showSnackbar(R.string.success)
                    resetPasswordState.value = ScreenState.Success
                }

                is Result.Error -> {
                    snackbarController.showSnackbar(result.error.messageRes)
                    resetPasswordState.value = ScreenState.Default
                }
            }
        }
    }

    private fun onInputComplete() {
        passwordState.update {
            TextFieldState(it.text, it.text.validateInputValue(FieldType.PASSWORD))
        }
    }

    @Immutable
    data class State(
        val resetPasswordState: ScreenState = ScreenState.Default,
        val password: TextFieldState = TextFieldState(),
        val onPasswordChanged: (String) -> Unit = {},
        val onInputComplete: () -> Unit = {},
        val isResetBtnEnabled: Boolean = false,
        val onResetPasswordClicked: () -> Unit = {},
    )
}
