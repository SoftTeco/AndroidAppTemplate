package com.softteco.template.data.base

import com.softteco.template.data.base.error.ErrorBody
import kotlinx.coroutines.delay

sealed interface ApiResult<T : Any>

class ApiSuccess<T : Any>(val code: Int, val data: T) : ApiResult<T>
class ApiError<T : Any>(
    val code: Int,
    val errorBody: ErrorBody?
) : ApiResult<T>

class ApiException<T : Any>(val e: Throwable) : ApiResult<T>

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (data: T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (code: Int, errorBody: ErrorBody?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiError<T>) {
        executable(code, errorBody)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiException<T>) {
        executable(e)
    }
}

fun <T : Any, R : Any> ApiResult<T>.map(
    transform: (data: T) -> R
): ApiResult<R> {
    return when (this) {
        is ApiError -> ApiError(code, errorBody)
        is ApiException -> ApiException(e)
        is ApiSuccess -> ApiSuccess(code, transform(data))
    }
}

suspend inline fun <T : Any> requestWithRetry(
    maxRetries: Int = 1,
    delay: Long = 0L,
    block: () -> ApiResult<T>
): ApiResult<T> {
    var retries = 0
    var result = block()
    if (result is ApiException) {
        while (retries < maxRetries) {
            retries++
            if (delay > 0) delay(delay)
            result = block()
        }
    }
    return result
}
