package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class CreateUserDto(
    @field:Json(name = "firstName") val firstName: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "password") val password: String,
    @field:Json(name = "confirmPassword") val confirmPassword: String,
)
