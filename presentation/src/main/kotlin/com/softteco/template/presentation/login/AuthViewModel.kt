package com.softteco.template.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.user.*
import com.softteco.template.domain.repository.user.LoginResponse
import com.softteco.template.domain.repository.user.RegisterResponse
import com.softteco.template.domain.repository.user.RestorePasswordResponse
import com.softteco.template.domain.repository.user.ResetPasswordResponse

import com.softteco.template.domain.usecase.user.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val useCase: UseCases) : ViewModel() {

    var loginResponse by mutableStateOf<LoginResponse>(Response.Success(false))

    var registerResponse by mutableStateOf<RegisterResponse>(Response.Success(false))

    var restorePasswordResponse by mutableStateOf<RestorePasswordResponse>(Response.Success(false))

    var resetPasswordResponse by mutableStateOf<ResetPasswordResponse>(Response.Success(false))

    fun login(
        userAuth: LoginAuthDto
    ) = viewModelScope.launch {
        loginResponse = Response.Loading
        loginResponse =
            useCase.login(userAuth)
    }

    fun register(
        user: CreateUserDto
    ) {
        viewModelScope.launch {
            registerResponse = Response.Loading
            registerResponse =
                useCase.register(
                    user
                )
        }
    }

    fun restorePassword(email: ForgotPasswordDto) {
        viewModelScope.launch {
            restorePasswordResponse = Response.Loading
            restorePasswordResponse = useCase.restorePassword(email)
        }
    }

    fun resetPassword(resetPasswordDto: ResetPasswordDto) {
        viewModelScope.launch {
            resetPasswordResponse = Response.Loading
            resetPasswordResponse = useCase.resetPassword(resetPasswordDto)
        }
    }
}

