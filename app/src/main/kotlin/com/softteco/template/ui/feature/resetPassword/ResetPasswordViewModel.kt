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
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.ScreenState
import com.softteco.template.ui.feature.validatePassword
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {

    private val resetPasswordState =
        MutableStateFlow<ScreenState>(ScreenState.Default)
    private var passwordStateValue = MutableStateFlow("")
    private val token: String = checkNotNull(savedStateHandle[AppNavHost.RESET_TOKEN_ARG])

    val state = combine(
        resetPasswordState,
        passwordStateValue,
    ) { resetPasswordState, passwordValue ->
        val passwordState = validatePassword(passwordValue)
        State(
            resetPasswordState = resetPasswordState,
            passwordValue = passwordValue,
            fieldStatePassword = passwordState,
            isResetBtnEnabled = passwordState is PasswordFieldState.Success,
            onPasswordChanged = { passwordStateValue.value = it },
            onResetPasswordClicked = ::onResetPassword
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    private fun onResetPassword() {
        viewModelScope.launch(appDispatchers.ui) {
            resetPasswordState.value = ScreenState.Loading

            val newPassword = NewPasswordDto(
                password = passwordStateValue.value,
                confirmation = passwordStateValue.value,
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

    @Immutable
    data class State(
        val resetPasswordState: ScreenState = ScreenState.Default,
        val passwordValue: String = "",
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Empty,
        val isResetBtnEnabled: Boolean = false,
        val onPasswordChanged: (String) -> Unit = {},
        val onResetPasswordClicked: () -> Unit = {},
    )
}
