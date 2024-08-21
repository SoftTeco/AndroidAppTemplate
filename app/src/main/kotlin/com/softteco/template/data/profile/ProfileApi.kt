package com.softteco.template.data.profile

import com.softteco.template.data.base.ApiResult
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.UpdateUserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface ProfileApi {

    companion object {
        const val AUTH_HEADER = "Authorization"
    }

    @GET("/profile/me")
    suspend fun getUser(@Header(AUTH_HEADER) authHeader: String): ApiResult<ProfileDto>

    @PATCH("/profile/user")
    suspend fun updateUser(
        @Header(AUTH_HEADER) authHeader: String,
        @Body updateUserDto: UpdateUserDto
    ): ApiResult<ProfileDto>
}
