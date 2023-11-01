package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class NewUserDto(
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "email")
    val email: String,
)
