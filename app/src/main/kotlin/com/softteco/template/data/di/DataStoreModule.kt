package com.softteco.template.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.softteco.template.data.profile.dto.AuthTokenDto
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.utils.AuthTokenSerializer
import com.softteco.template.utils.CryptoManager
import com.softteco.template.utils.UserProfileSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val PREFERENCES = "preferences"
private const val USER_PROFILE_ENCRYPTED = "user_profile_encrypted_data"
private const val AUTH_TOKEN_ENCRYPTED = "auth_token_encrypted"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<AuthTokenDto> {
        return DataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { AuthTokenDto("") }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.dataStoreFile(AUTH_TOKEN_ENCRYPTED) },
            serializer = AuthTokenSerializer(CryptoManager())
        )
    }

    @Provides
    @Singleton
    fun provideUserProfileEncryptedDataStore(@ApplicationContext appContext: Context): DataStore<ProfileDto> {
        return DataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { ProfileDto(0, "", "", "", "") }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.dataStoreFile(USER_PROFILE_ENCRYPTED) },
            serializer = UserProfileSerializer(CryptoManager())
        )
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(PREFERENCES) }
        )
    }
}
