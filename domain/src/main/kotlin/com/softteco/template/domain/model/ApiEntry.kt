package com.softteco.template.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiEntry(
    val id: Int? = null,
    val name: String,
    val auth: String,
    val category: String,
    val cors: String,
    val description: String,
    val https: Boolean,
    val link: String,
    val logo: String,
    val favorite: Boolean
) : Parcelable
