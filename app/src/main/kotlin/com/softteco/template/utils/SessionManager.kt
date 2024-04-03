package com.softteco.template.utils

import androidx.datastore.core.DataStore
import com.softteco.template.data.profile.dto.AuthTokenDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    authTokenEncryptedDataStore: DataStore<AuthTokenDto>
) {

    var isUserLoggedIn: Flow<Boolean> = authTokenEncryptedDataStore.data.map {
        it.token.isNotEmpty()
    }
}
