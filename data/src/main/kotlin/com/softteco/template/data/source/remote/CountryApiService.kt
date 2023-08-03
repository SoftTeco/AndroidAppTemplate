package com.softteco.template.data.source.remote

import com.softteco.template.domain.model.CountriesItem
import retrofit2.http.GET

interface CountryApiService {
    @GET("countries")
    suspend fun getCountries(): CountriesItem
}