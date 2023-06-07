package com.softteco.template.domain.repository

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.user.RegisterResponse

interface AccountRepository {
    suspend fun addAccount (account: Account): RegisterResponse
}