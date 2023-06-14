package com.softteco.template.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.domain.repository.user.RegisterResponse
import com.softteco.template.domain.usecase.account.UseCasesDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModelDb @Inject constructor(private val useCase: UseCasesDb) : ViewModel() {

    var registerApiResponse by mutableStateOf<RegisterResponse>(ApiResponse.Success(false))

    var restorePasswordApiResponse by mutableStateOf<RegisterResponse>(ApiResponse.Success(false))

    fun register(account: Account) {
        viewModelScope.launch {
            registerApiResponse = ApiResponse.Loading
            registerApiResponse =
                useCase.register(
                account
                )
        }
    }

    fun restorePassword(account: Account) {
        viewModelScope.launch {
            registerApiResponse = ApiResponse.Loading
            registerApiResponse =
                useCase.register(
                    account
                )
        }
    }
}