package com.softteco.template.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.CountriesItem
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.domain.usecase.user.CountryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val useCase: CountryUseCases) : ViewModel() {
    var countriesResponse by mutableStateOf<ApiResponse<CountriesItem>>(ApiResponse.Loading)

    init {
        getCountries()
    }

    private fun getCountries() = viewModelScope.launch {
        useCase.country().collect { response ->
                countriesResponse = response
            }
    }
}