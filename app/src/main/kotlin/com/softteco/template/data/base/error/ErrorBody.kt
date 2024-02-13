package com.softteco.template.data.base.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    @SerialName("code")
    val code: String,
    @SerialName("error_type")
    val errorType: String,
    @SerialName("message")
    val message: String
)
