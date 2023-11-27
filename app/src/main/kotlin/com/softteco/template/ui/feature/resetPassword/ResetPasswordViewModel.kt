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
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.validatePassword
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
class ResetPasswordViewModel @Inject constructor(
    private val repository: ProfileRepository,
    savedStateHandle: SavedStateHandle,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val resetPasswordState =
        MutableStateFlow<ResetPasswordState>(ResetPasswordState.Default)
    private var passwordStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val token: String = checkNotNull(savedStateHandle[AppNavHost.RESET_TOKEN_ARG])

    val state = combine(
        resetPasswordState,
        passwordStateValue,
        snackBarState,
    ) { resetPasswordState, passwordValue, snackBar ->
        val passwordState = validatePassword(passwordValue)
        State(
            resetPasswordState = resetPasswordState,
            passwordValue = passwordValue,
            fieldStatePassword = passwordState,
            snackBar = snackBar,
            isResetBtnEnabled = passwordState is PasswordFieldState.Success,
            dismissSnackBar = { snackBarState.value = SnackBarState() },
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
            resetPasswordState.value = ResetPasswordState.Loading

            val newPassword = NewPasswordDto(
                password = passwordStateValue.value,
                confirmation = passwordStateValue.value,
            )

            val result = repository.changePassword(token, newPassword)
            resetPasswordState.value = when (result) {
                is Result.Success -> {
                    snackBarState.value = SnackBarState(R.string.success, true)
                    ResetPasswordState.Success
                }

                is Result.Error -> {
                    handleApiError(result, snackBarState)
                    ResetPasswordState.Default
                }
            }
        }
    }

    @Immutable
    data class State(
        val resetPasswordState: ResetPasswordState = ResetPasswordState.Default,
        val passwordValue: String = "",
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Empty,
        val snackBar: SnackBarState = SnackBarState(),
        val isResetBtnEnabled: Boolean = false,
        val onPasswordChanged: (String) -> Unit = {},
        val onResetPasswordClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {},
    )

    @Immutable
    sealed class ResetPasswordState {
        object Default : ResetPasswordState()
        object Loading : ResetPasswordState()
        object Success : ResetPasswordState()
    }
}
