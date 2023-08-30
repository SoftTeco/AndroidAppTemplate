package com.softteco.template.data.di

import com.softteco.template.data.base.error.ErrorHandler
import com.softteco.template.data.base.error.ErrorHandlerImpl
import com.softteco.template.data.login.CreateUserRepository
import com.softteco.template.data.login.CreateUserRepositoryImpl
import com.softteco.template.data.login.LoginRepository
import com.softteco.template.data.login.LoginRepositoryImpl
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.ProfileRepositoryImpl
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
	fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

	@Binds
	fun bindCreateUserRepository(impl: CreateUserRepositoryImpl): CreateUserRepository

	@Binds
	fun bindErrorHandler(impl: ErrorHandlerImpl): ErrorHandler
}
