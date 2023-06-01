package com.softteco.template.domain.repository

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.user.LoginResponse
import com.softteco.template.domain.repository.user.RegisterResponse
import com.softteco.template.domain.repository.user.RestorePasswordResponse

interface AccountRepository {
    suspend fun addAccount (account: Account): RegisterResponse

    suspend fun loginAccount (account: Account): LoginResponse //TODO

    suspend fun restorePasswordAccount (account: Account): RestorePasswordResponse //TODO
}