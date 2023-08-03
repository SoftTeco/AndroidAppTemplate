package com.softteco.template.data.repository.user

import com.softteco.template.data.source.remote.CountryApiService
import com.softteco.template.domain.model.CountriesItem
import com.softteco.template.domain.repository.user.CountryRepository

import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val apiService: CountryApiService
) : CountryRepository {
    override suspend fun getCountries(): CountriesItem {
        return apiService.getCountries()
    }
}