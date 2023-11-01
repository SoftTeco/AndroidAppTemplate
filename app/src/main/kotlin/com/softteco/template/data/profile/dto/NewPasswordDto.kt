package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class NewPasswordDto(
    @Json(name = "password")
    val password: String,
    @Json(name = "confirmation")
    val confirmation: String
)
