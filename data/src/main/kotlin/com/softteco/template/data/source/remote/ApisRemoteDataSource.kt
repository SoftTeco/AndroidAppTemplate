package com.softteco.template.data.source.remote

import com.softteco.template.data.BaseRemoteDataSource
import com.softteco.template.data.source.remote.model.ApiEntryApiModels
import com.softteco.template.domain.model.Output
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/**
 * RemoteDataSource of Apis API service
 * @param publicApi the object of api service
 */
class ApisRemoteDataSource @Inject constructor(@Named("PublicApi")
    retrofit: Retrofit,
    private val publicApi: PublicApi
) : BaseRemoteDataSource(retrofit) {

    /**
     * Method to fetch the Apis from ApisRemoteDataSource
     * @return Outputs with list of Apis
     */
    internal suspend fun fetchEntries(): Output<ApiEntryApiModels> {
        return getResponse(
            request = { publicApi.getAllEntries() },
            defaultErrorMessage = "Error fetching Apis"
        )
    }
}
