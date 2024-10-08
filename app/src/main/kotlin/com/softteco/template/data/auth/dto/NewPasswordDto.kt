package com.softteco.template.data.auth.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewPasswordDto(
    @SerialName("password")
    val password: String,
    @SerialName("confirmation")
    val confirmation: String
)
