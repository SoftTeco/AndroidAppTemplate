package com.softteco.template.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.domain.model.user.Response
import com.softteco.template.domain.repository.AccountRepository
import com.softteco.template.domain.repository.user.RegisterResponse
import com.softteco.template.domain.usecase.account.UseCasesDb
import com.softteco.template.domain.usecase.user.UseCases
import com.softteco.template.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModelDb @Inject constructor(private val useCase: UseCasesDb) : ViewModel() {

    private val _saveRandomDataOutput = MutableStateFlow<Output.Status?>(null)
    internal val saveRandomDataOutput: StateFlow<Output.Status?> = _saveRandomDataOutput

    var registerResponse by mutableStateOf<RegisterResponse>(Response.Success(false))

//    internal fun save(account: Account) {
//        viewModelScope.launch {
//            accountRepository.addAccount(account).collect {
//                _saveRandomDataOutput.value = it
//            }
//        }
//    }

    fun register(account: Account) {
        viewModelScope.launch {
            registerResponse = Response.Loading
            registerResponse =
                useCase.register(
                account
                )
        }
    }
}