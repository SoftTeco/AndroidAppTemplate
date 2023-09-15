package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class ForgotPasswordDto(@field:Json(name = "email") val email: String)
