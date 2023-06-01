package com.softteco.template.domain.usecase.account

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.AccountRepository

class RegistrationDb(private val repository: AccountRepository) {
    suspend operator fun invoke(
        user: Account
    ) = repository.addAccount(
        user
    )
}