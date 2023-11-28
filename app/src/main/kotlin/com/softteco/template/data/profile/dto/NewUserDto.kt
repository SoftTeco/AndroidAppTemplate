package com.softteco.template.data.profile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewUserDto(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
)
