package com.softteco.template.data.source.remote

import com.softteco.template.data.source.remote.model.ApiEntryApiModels
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API Service
 */
interface PublicApi {

    @GET("/entries")
    suspend fun getAllEntries(): Response<ApiEntryApiModels>
}
