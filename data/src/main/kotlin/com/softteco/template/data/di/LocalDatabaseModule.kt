package com.softteco.template.data.di

import android.content.Context
import com.softteco.template.data.source.local.AccountDatabase
import com.softteco.template.data.source.local.EntryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = EntryDatabase.create(appContext)

    @Provides
    fun provideApiEntryDao(entryDatabase: EntryDatabase) = entryDatabase.apiEntryDao()

    @Singleton
    @Provides
    fun provideAccountDatabase(@ApplicationContext appContext: Context) = AccountDatabase.create(appContext)

    @Provides
    fun provideAccountDao(accountDatabase: AccountDatabase) = accountDatabase.accountDao()

}
