package com.softteco.template.data.di

import com.softteco.template.data.repository.ApisRepositoryImpl
import com.softteco.template.data.repository.user.AccountRepositoryImpl
import com.softteco.template.domain.repository.AccountRepository
import com.softteco.template.domain.repository.ApisRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule {
    @Binds
    @Singleton
    internal abstract fun bindApisRepository(repository: ApisRepositoryImpl): ApisRepository

    @Binds
    @Singleton
    internal abstract fun bindAccountRepository(repository: AccountRepositoryImpl): AccountRepository
}
