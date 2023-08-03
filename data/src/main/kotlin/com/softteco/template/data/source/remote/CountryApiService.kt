package com.softteco.template.data.source.remote

import com.softteco.template.domain.model.CountriesItem
import retrofit2.Response
import retrofit2.http.GET

interface  CountryApiService {
    @GET("countries")
   fun getCountries(): Response<CountriesItem>
}