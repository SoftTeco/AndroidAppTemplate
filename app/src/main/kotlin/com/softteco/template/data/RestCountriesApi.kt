package com.softteco.template.data

import com.softteco.template.data.profile.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesApi {
    companion object {
        const val BASE_URL = "https://restcountries.com"
    }

    @GET("/v3.1/name/{name}")
    suspend fun getCountryList(@Path("name") name: String): List<CountryDto>
}
