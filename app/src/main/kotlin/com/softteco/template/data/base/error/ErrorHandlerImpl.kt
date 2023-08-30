package com.softteco.template.data.base.error

import android.util.Log
import com.softteco.template.data.base.error.ErrorEntity.Companion.API_ERROR
import com.softteco.template.data.base.error.ErrorEntity.Companion.NETWORK_ERROR
import com.softteco.template.data.base.error.ErrorEntity.Companion.UNKNOWN_ERROR
import okio.IOException
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> {
                Log.e(NETWORK_ERROR, throwable.toString())
                ErrorEntity.Network
            }

            is HttpException -> {
                Log.e(API_ERROR, throwable.toString())
                when (throwable.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

                    else -> ErrorEntity.Unknown
                }
            }

            else -> {
                Log.e(UNKNOWN_ERROR, throwable.toString())
                ErrorEntity.Unknown
            }
        }
    }
}
