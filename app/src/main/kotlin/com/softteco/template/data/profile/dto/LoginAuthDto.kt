package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class LoginAuthDto(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "password") val password: String
)
