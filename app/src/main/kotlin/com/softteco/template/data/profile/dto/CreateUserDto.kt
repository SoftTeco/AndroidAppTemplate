package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDto(
    @Json(name = "username")
    val username: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
)
