package com.softteco.template.presentation.login

import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.AccountRepository
import com.softteco.template.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModelDb@Inject constructor(
    private val accountRepository: AccountRepository,
) : BaseViewModel() {

    private val _saveRandomDataOutput = MutableStateFlow<Output.Status?>(null)
    internal val saveRandomDataOutput: StateFlow<Output.Status?> = _saveRandomDataOutput

    internal fun save(account: Account) {
        viewModelScope.launch {
            accountRepository.addAccount(account).collect {
                _saveRandomDataOutput.value = it
            }
        }
    }
}