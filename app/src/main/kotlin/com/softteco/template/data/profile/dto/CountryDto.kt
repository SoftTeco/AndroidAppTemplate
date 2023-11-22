package com.softteco.template.data.profile.dto

import com.squareup.moshi.Json

data class CountryDto(
    @Json(name = "name")
    val name: Name,
) {
    data class Name(
        @Json(name = "common")
        val common: String,
        @Json(name = "official")
        val official: String,
    )
}
