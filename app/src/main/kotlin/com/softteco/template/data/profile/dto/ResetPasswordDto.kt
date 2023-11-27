package com.softteco.template.data.profile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordDto(
    @SerialName("email")
    val email: String
)
