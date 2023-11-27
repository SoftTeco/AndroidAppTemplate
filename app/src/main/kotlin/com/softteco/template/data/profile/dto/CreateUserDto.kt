package com.softteco.template.data.profile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)
