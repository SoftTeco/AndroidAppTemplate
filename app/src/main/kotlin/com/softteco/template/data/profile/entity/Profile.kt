package com.softteco.template.data.profile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    val avatar: String? = null,
) {
    companion object {

        fun Profile.toJson(): String = Json.encodeToString(this)

        fun fromJson(json: String): Profile = Json.decodeFromString(json)
    }

    fun fullName(): String {
        return StringBuilder()
            .append(firstName ?: "")
            .append(" ")
            .append(lastName ?: "")
            .toString()
            .ifBlank { "" }
    }
}
