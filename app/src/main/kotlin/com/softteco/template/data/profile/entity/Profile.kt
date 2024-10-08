package com.softteco.template.data.profile.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.softteco.template.data.profile.dto.UpdateUserDto
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
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

fun Profile.toUpdateUserDto(): UpdateUserDto {
    return UpdateUserDto(
        firstName = firstName,
        lastName = lastName,
        country = country,
        birthDate = birthDate
    )
}
