package com.softteco.template.data.profile.dto

import com.softteco.template.data.profile.entity.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("birth_date")
    val birthDate: String? = null,
    @SerialName("country")
    val country: String? = null,
)

fun ProfileDto.toModel(): Profile {
    return Profile(
        id = id,
        username = username,
        email = email,
        createdAt = createdAt,
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate,
        country = country,
    )
}
