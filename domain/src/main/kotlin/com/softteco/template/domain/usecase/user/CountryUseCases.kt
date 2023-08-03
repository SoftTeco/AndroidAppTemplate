package com.softteco.template.domain.usecase.user

import com.softteco.template.domain.model.CountriesData
import com.softteco.template.domain.model.toDataObject
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.domain.repository.user.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

class CountryUseCases @Inject constructor(private val repository: CountryRepository) {
    operator fun invoke(): Flow<ApiResponse<List<CountriesData>>> = flow {
        try {
            emit(ApiResponse.Loading)
            val data = repository.getCountries().data.map {
                it.toDataObject()
            }
            emit(ApiResponse.Success(data))
        } catch (e: HttpRetryException) {
            emit(ApiResponse.Failure(e))
        } catch (e: IOException) {
            emit(ApiResponse.Failure(e))
        }
    }
}