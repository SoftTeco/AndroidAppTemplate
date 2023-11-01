package com.softteco.template.data.profile.dto

import com.softteco.template.data.profile.entity.Profile
import com.squareup.moshi.Json

data class ProfileDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "username")
    val username: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "first_name")
    val firstName: String? = null,
    @Json(name = "last_name")
    val lastName: String? = null,
    @Json(name = "birth_date")
    val birthDate: String? = null,
    @Json(name = "country")
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
