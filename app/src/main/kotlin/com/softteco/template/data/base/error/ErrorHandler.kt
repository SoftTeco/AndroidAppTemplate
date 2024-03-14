package com.softteco.template.data.base.error

import com.softteco.template.data.base.ApiError
import com.softteco.template.data.base.ApiException
import com.softteco.template.data.base.ApiResult
import com.softteco.template.data.base.ApiSuccess

fun <T : Any> handleError(result: ApiResult<T>): Result.Error<T> {
    return when (result) {
        is ApiError -> Result.Error(AppError.AuthError.findByCode(result.errorBody?.code))
        is ApiException -> Result.Error(AppError.NetworkError())
        is ApiSuccess -> throw IllegalArgumentException("ApiSuccess should not be handled in handleError")
    }
}
