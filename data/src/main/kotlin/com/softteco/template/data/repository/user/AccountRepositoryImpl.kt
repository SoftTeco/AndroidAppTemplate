package com.softteco.template.data.repository.user

import com.softteco.template.data.repository.base.BaseRepository
import com.softteco.template.data.source.local.AccountDao
import com.softteco.template.data.source.local.model.AccountEntity
import com.softteco.template.data.source.local.model.toDomainModel
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
) : AccountRepository, BaseRepository() {


    override val accountEntry: Flow<Account> = accountDao.getAccount()
        .map { it.toDomainModel() }

    override suspend fun addAccount(account: Account): Flow<Output.Status> {
        return flow {
            emit(Output.Status.LOADING)
            accountDao.add(AccountEntity.fromAccount(account))
            emit(Output.Status.SUCCESS)
        }.flowOn(Dispatchers.IO)
    }
}