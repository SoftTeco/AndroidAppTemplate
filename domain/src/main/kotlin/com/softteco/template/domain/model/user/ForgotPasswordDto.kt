package com.softteco.template.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotPasswordDto( val email: String): Parcelable
