package com.softteco.template.data.profile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    @PrimaryKey
    val id: String,
    val name: String,
)
