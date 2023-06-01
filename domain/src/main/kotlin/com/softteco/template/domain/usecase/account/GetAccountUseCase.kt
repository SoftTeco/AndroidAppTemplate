package com.softteco.template.domain.usecase.account

import com.softteco.template.domain.model.user.Account
import kotlinx.coroutines.flow.Flow

interface GetAccountUseCase {
    operator fun invoke(): Flow<Account>
}