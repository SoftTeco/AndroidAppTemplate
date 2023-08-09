package com.softteco.template.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.CountriesListState
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.domain.usecase.user.CountryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val useCase: CountryUseCases) : ViewModel() {

    private val stateGettingListCountry = mutableStateOf(CountriesListState())
    val state: State<CountriesListState> = stateGettingListCountry

    init {
        getList()
    }
    private fun getList() {
        useCase().onEach {
            when (it) {
                is ApiResponse.Success -> {
                    stateGettingListCountry.value = CountriesListState(list = it.data)
                }
                is ApiResponse.Loading -> {
                    stateGettingListCountry.value = CountriesListState(isLoading = true)
                }
                is ApiResponse.Failure -> {
                    stateGettingListCountry.value = CountriesListState(error = it.e?.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}