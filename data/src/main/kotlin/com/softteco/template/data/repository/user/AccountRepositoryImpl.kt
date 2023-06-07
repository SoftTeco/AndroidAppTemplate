package com.softteco.template.data.repository.user

import com.softteco.template.data.repository.base.BaseRepository
import com.softteco.template.data.source.local.AccountDao
import com.softteco.template.data.source.local.model.AccountEntity
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.Response
import com.softteco.template.domain.repository.AccountRepository
import com.softteco.template.domain.repository.user.RegisterResponse
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
) : AccountRepository, BaseRepository() {
    override suspend fun addAccount(
        account: Account
    ): RegisterResponse {
        return try {
            accountDao.add(
                AccountEntity.fromAccount(account)
            )
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}