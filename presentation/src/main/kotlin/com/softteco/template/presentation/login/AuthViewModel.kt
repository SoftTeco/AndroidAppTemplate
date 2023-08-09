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

    var loginApiResponse by mutableStateOf<LoginResponse>(ApiResponse.Success(false))

    var registerApiResponse by mutableStateOf<RegisterResponse>(ApiResponse.Success(false))

    var restorePasswordApiResponse by mutableStateOf<RestorePasswordResponse>(
        ApiResponse.Success(
            false
        )
    )

    var resetPasswordApiResponse by mutableStateOf<ResetPasswordResponse>(ApiResponse.Success(false))


    fun login(
        userAuth: LoginAuthDto
    ) = viewModelScope.launch {
        loginApiResponse = ApiResponse.Loading
        loginApiResponse =
            useCase.login(userAuth)
    }

    fun register(
        user: CreateUserDto
    ) {
        viewModelScope.launch {
            registerApiResponse = ApiResponse.Loading
            registerApiResponse =
                useCase.register(
                    user
                )
        }
    }

    fun restorePassword(email: ForgotPasswordDto) {
        viewModelScope.launch {
            restorePasswordApiResponse = ApiResponse.Loading
            restorePasswordApiResponse = useCase.restorePassword(email)
        }
    }

    fun resetPassword(resetPasswordDto: ResetPasswordDto) {
        viewModelScope.launch {
            resetPasswordApiResponse = ApiResponse.Loading
            resetPasswordApiResponse = useCase.resetPassword(resetPasswordDto)
        }
    }
}

