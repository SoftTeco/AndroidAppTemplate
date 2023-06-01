package com.softteco.template.domain.usecase.account

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository
) : GetAccountUseCase {

    override fun invoke(): Flow<Account> = accountRepository.accountEntry
}
