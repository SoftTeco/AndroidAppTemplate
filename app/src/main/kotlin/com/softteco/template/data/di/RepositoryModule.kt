package com.softteco.template.data.di

import com.softteco.template.data.auth.repository.AuthRepository
import com.softteco.template.data.auth.repository.AuthRepositoryImpl
import com.softteco.template.data.profile.repository.ProfileRepository
import com.softteco.template.data.profile.repository.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindFeature1Repository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
