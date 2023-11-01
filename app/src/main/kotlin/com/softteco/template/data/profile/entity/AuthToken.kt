package com.softteco.template.data.profile.entity

private const val TOKEN_SCHEME = "Bearer"

data class AuthToken(val token: String) {
    fun composeHeader(): String = "$TOKEN_SCHEME $token"
}
