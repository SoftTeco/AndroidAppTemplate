package com.softteco.template.domain.repository

import com.softteco.template.domain.model.Output
import com.softteco.template.domain.model.user.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val accountEntry: Flow<Account>
    suspend fun addAccount (account: Account): Flow<Output.Status>
}