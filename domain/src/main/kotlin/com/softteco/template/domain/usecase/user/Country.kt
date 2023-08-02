package com.softteco.template.domain.usecase.user


import com.softteco.template.domain.repository.user.CountryRepository

class Country (private val repository: CountryRepository) {
    suspend operator fun invoke() = repository.getCountries()
}