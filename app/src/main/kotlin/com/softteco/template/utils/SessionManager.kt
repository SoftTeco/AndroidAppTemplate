package com.softteco.template.utils

import androidx.datastore.core.DataStore
import com.softteco.template.data.profile.dto.AuthTokenDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val authTokenEncryptedDataStore: DataStore<AuthTokenDto>
) {

    private val _tokenStateFlow = MutableStateFlow("")

    val tokenStateFlow: StateFlow<String>
        get() = _tokenStateFlow

    suspend fun observeTokenUpdates(): MutableStateFlow<String> {
        authTokenEncryptedDataStore.data.first().let { authTokenDto ->
            _tokenStateFlow.value = authTokenDto.token
        }
        authTokenEncryptedDataStore.data.collect { authTokenDto ->
            _tokenStateFlow.value = authTokenDto.token
        }
        return _tokenStateFlow
    }
}
