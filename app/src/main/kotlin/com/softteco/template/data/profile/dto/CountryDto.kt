package com.softteco.template.data.profile.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("name")
    val name: Name,
) {
    @Serializable
    data class Name(
        @SerialName("common")
        val common: String,
        @SerialName("official")
        val official: String,
    )
}
