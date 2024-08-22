package com.softteco.template.data.base.interceptors

import androidx.datastore.core.DataStore
import com.softteco.template.data.auth.dto.AuthTokenDto
import com.softteco.template.data.auth.dto.toModel
import com.softteco.template.data.auth.entity.AuthToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import timber.log.Timber

private const val AUTH_HEADER = "Authorization"

class AuthTokenInterceptor(
    private val authTokenEncryptedDataStore: DataStore<AuthTokenDto>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = runBlocking {
            try {
                authTokenEncryptedDataStore.data.first().toModel()
            } catch (e: IOException) {
                Timber.e(e)
                AuthToken("")
            }
        }
        // could be checked if token is empty

        val request = chain
            .request()
            .newBuilder()
            .header(AUTH_HEADER, authToken.composeHeader())
            .build()

        // could be updated to handle 401 error

        return chain.proceed(request)
    }
}
