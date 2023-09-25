package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class ResetPasswordDto(
    @field:Json(name = "token") val token: String,
    @field:Json(name = "password") val password: String,
    @field:Json(name = "confirmPassword") val confirmPassword: String
)
