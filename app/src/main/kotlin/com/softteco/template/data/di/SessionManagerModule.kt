package com.softteco.template.data.di

import androidx.datastore.core.DataStore
import com.softteco.template.data.profile.dto.AuthTokenDto
import com.softteco.template.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionManagerModule {
    @Provides
    @Singleton
    fun provideSessionManagerProvider(authTokenEncryptedDataStore: DataStore<AuthTokenDto>): SessionManager {
        return SessionManager(authTokenEncryptedDataStore)
    }
}
