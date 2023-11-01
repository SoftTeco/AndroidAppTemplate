package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class CredentialsDto(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)
