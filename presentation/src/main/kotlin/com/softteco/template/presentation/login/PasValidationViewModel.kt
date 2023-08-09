package com.softteco.template.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.presentation.login.loginComponents.registration.PasswordValidationState
import com.softteco.template.presentation.login.loginComponents.registration.ValidatePassword
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class PasValidationViewModel(
    private val validatePassword: ValidatePassword = ValidatePassword()
) : ViewModel() {
    var password by mutableStateOf("")
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val passwordError =
        snapshotFlow { password }
            .mapLatest { validatePassword.execute(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PasswordValidationState()
            )

    fun changePassword(value: String) {
        password = value
    }
}