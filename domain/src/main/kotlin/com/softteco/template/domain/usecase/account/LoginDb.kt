package com.softteco.template.domain.usecase.account

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.AccountRepository

class LoginDb(private val repository: AccountRepository) {
    suspend operator fun invoke(
        userAuth: Account
    ) = repository.loginAccount(
        userAuth
    )
}