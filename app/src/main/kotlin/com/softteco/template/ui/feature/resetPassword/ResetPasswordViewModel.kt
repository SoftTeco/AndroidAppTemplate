package com.softteco.template.ui.feature.resetPassword

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.navigation.AppNavHost
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.feature.PasswordFieldState
import com.softteco.template.ui.feature.ValidateFields.isHasCapitalizedLetter
import com.softteco.template.ui.feature.ValidateFields.isHasMinimum
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val loading = MutableStateFlow(false)
    private val resetPasswordState = MutableStateFlow(false)
    private var passwordStateValue = MutableStateFlow("")
    private var snackBarState = MutableStateFlow(SnackBarState())
    private val token: String = checkNotNull(savedStateHandle[AppNavHost.RESET_TOKEN_ARG])

    val state = combine(
        loading,
        passwordStateValue,
        snackBarState,
    ) { loading, passwordValue, snackBar ->
        State(
            loading = loading,
            passwordValue = passwordValue,
            fieldStatePassword = when {
                passwordValue.isEmpty() -> PasswordFieldState.Empty
                else -> PasswordFieldState.Success
            },
            isPasswordHasMinimum = passwordValue.isHasMinimum(),
            isPasswordHasUpperCase = passwordValue.isHasCapitalizedLetter(),
            snackBar = snackBar,
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
        loading.value = true
        val isAllFieldsValid = state.value.run {
            fieldStatePassword is PasswordFieldState.Success &&
                isPasswordHasUpperCase && isPasswordHasMinimum
        }
        if (isAllFieldsValid) {
            viewModelScope.launch {
                val resetPasswordDto = ResetPasswordDto(
                    token = token,
                    password = passwordStateValue.value,
                    confirmPassword = passwordStateValue.value,
                )
                when (val result = repository.resetPassword(resetPasswordDto)) {
                    is Result.Success -> resetPasswordState.value = true
                    is Result.Error -> handleApiError(result, snackBarState)
                }
            }
        } else {
            snackBarState.value = SnackBarState(
                R.string.empty_fields_error,
                true
            )
        }
        loading.value = false
    }

    @Immutable
    data class State(
        val loading: Boolean = false,
        val resetPasswordState: Boolean = false,
        val passwordValue: String = "",
        val fieldStatePassword: PasswordFieldState = PasswordFieldState.Waiting,
        val isPasswordHasMinimum: Boolean = false,
        val isPasswordHasUpperCase: Boolean = false,
        val snackBar: SnackBarState = SnackBarState(),
        val onPasswordChanged: (String) -> Unit = {},
        val onResetPasswordClicked: () -> Unit = {},
        val dismissSnackBar: () -> Unit = {}
    )
}
