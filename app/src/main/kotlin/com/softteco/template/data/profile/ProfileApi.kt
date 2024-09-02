package com.softteco.template.data.profile

import com.softteco.template.data.auth.dto.NewPasswordDto
import com.softteco.template.data.base.ApiResult
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.UpdateUserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface ProfileApi {

    @PUT("/profile/password")
    suspend fun updatePassword(
        @Body newPassword: NewPasswordDto
    ): ApiResult<Unit>

    @GET("/profile/me")
    suspend fun getUser(): ApiResult<ProfileDto>

    @PATCH("/profile/user")
    suspend fun updateUser(
        @Body updateUserDto: UpdateUserDto
    ): ApiResult<ProfileDto>
}
