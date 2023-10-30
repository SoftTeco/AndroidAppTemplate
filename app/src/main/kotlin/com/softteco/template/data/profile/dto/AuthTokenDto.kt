package com.softteco.template.data.profile.dto

import com.softteco.template.data.profile.entity.AuthToken
import com.squareup.moshi.Json

data class AuthTokenDto(
    @Json(name = "token")
    val token: String,
)

fun AuthTokenDto.toModel(): AuthToken {
    return AuthToken(token)
}
