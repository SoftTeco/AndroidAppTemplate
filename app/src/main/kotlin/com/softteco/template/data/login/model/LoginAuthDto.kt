package com.softteco.template.data.login.model

import com.squareup.moshi.Json

data class LoginAuthDto(
	@field:Json(name = "email") val email: String,
	@field:Json(name = "password") val password: String
)
