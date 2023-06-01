package com.softteco.template.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val country: String,
    val birthday: String,
    val email: String,
    val password: String,
    val avatar: String
) : Parcelable
