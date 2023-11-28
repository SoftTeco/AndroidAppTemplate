package com.softteco.template.data.profile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)
