package com.softteco.template.domain.repository.user

import com.softteco.template.domain.model.CountriesItem

interface CountryRepository {
    suspend fun getCountries(): CountriesItem
}
