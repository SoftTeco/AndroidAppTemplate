package com.softteco.template.data.base.error

sealed class Result<T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error<T>(val error: AppError) : Result<T>()

    val isSuccess: Boolean get() = this !is Error

    val isFailure: Boolean get() = this is Error
}
