package com.softteco.template.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResetPasswordDto(
    val token: String,
    val password: String,
    val confirmPassword: String
) : Parcelable