package com.softteco.template.data.base

import com.softteco.template.data.base.error.ErrorBody
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

@Suppress("TooGenericExceptionCaught")
fun <T : Any> handleApiResponse(response: Response<T>): ApiResult<T> {
    return try {
        val body = response.body()

        if (response.isSuccessful && body != null) {
            ApiSuccess(code = response.code(), data = body)
        } else {
            ApiError(
                code = response.code(),
                errorBody = response.toError(),
            )
        }
    } catch (e: HttpException) {
        ApiError(code = e.code(), ErrorBody(e.cause.toString(), e::class.toString(), e.message()))
    } catch (e: Throwable) {
        ApiException(e)
    }
}

@Suppress("TooGenericExceptionCaught")
private fun <T : Any> Response<T>.toError(): ErrorBody? {
    return try {
        errorBody()?.string()?.let { Json.decodeFromString<ErrorBody>(it) }
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}
