package com.softteco.template.data.profile

import com.softteco.template.data.base.ApiResult
import com.softteco.template.data.profile.dto.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {

    companion object {
        const val AUTH_HEADER = "Authorization"
    }

    @GET("/profile/me")
    suspend fun getUser(@Header(AUTH_HEADER) authHeader: String): ApiResult<ProfileDto>
}
