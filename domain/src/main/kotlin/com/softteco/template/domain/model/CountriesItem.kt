package com.softteco.template.domain.model

data class CountriesItem
    (
    val error: Boolean,
    val msg: String,
    val data: List<DataObject>
)

data class DataObject(
    val cities: List<String>,
    val country: String
)

data class CountriesData(
    val countryName: String
)

data class CountriesListState(
    val isLoading: Boolean = false,
    val list: List<CountriesData> = emptyList(),
    val error: String = ""
)

fun DataObject.toDataObject(): CountriesData {
    return CountriesData(countryName = country)
}