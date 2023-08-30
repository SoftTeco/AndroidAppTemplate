package com.softteco.template.data.base.error

sealed class ErrorEntity(val isDisplayable: Boolean) {
    companion object {
        const val NETWORK_ERROR = "Network Error"
        const val API_ERROR = "API Error"
        const val UNKNOWN_ERROR = "Unknown Error"
    }

    object Network : ErrorEntity(true)

    object AccessDenied : ErrorEntity(false)

    object NotFound : ErrorEntity(false)

    object ServiceUnavailable : ErrorEntity(false)

    object Unknown : ErrorEntity(true)
}
