package com.softteco.template.data.base.error

interface ErrorHandler {

    fun getError(throwable: Throwable): ErrorEntity
}
