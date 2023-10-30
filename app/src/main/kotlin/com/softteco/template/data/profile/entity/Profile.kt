package com.softteco.template.data.profile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    val createdAt: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: String? = null,
    val country: String? = null,
)
