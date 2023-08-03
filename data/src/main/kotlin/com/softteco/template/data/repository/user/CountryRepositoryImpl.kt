package com.softteco.template.data.repository.user

import com.softteco.template.data.source.remote.CountryApiService
import com.softteco.template.domain.model.CountriesItem
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.domain.repository.user.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val apiService: CountryApiService
) : CountryRepository {

    override fun getCountries(): Flow<ApiResponse<CountriesItem>> {
        return safeCall { apiService.getCountries() }
    }

    private fun <T> safeCall(
        apiCall: suspend () -> Response<T>
    ): Flow<ApiResponse<T>> = flow {
        emit(ApiResponse.Loading)
        val response = apiCall()
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                emit(ApiResponse.Success(data))
            } else {
                val error = response.errorBody()
                if (error != null) {
                    emit(ApiResponse.Failure(IOException(error.toString())))
                } else {
                    emit(ApiResponse.Failure(IOException("Wrong")))
                }
            }
        } else {
            emit(ApiResponse.Failure(Exception(response.errorBody().toString())))
        }
    }.catch { e ->
        emit(ApiResponse.Failure(Exception(e)))
    }.flowOn(Dispatchers.IO)
}