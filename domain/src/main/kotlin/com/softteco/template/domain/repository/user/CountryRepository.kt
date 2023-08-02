package com.softteco.template.domain.repository.user

import com.softteco.template.domain.model.CountriesItem
import com.softteco.template.domain.model.user.ApiResponse
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
   suspend fun getCountries(): Flow<ApiResponse<CountriesItem>>
}
