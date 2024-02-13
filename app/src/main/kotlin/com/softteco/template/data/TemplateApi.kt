package com.softteco.template.data

import com.softteco.template.data.base.ApiResult
import com.softteco.template.data.profile.dto.AuthTokenDto
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.dto.NewPasswordDto
import com.softteco.template.data.profile.dto.NewUserDto
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.ResetPasswordDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TemplateApi {

    companion object {
        const val AUTH_HEADER = "Authorization"
    }

    @GET("/profile/me")
    suspend fun getUser(@Header(AUTH_HEADER) authHeader: String): ApiResult<ProfileDto>

    @POST("/login")
    suspend fun login(@Body userAuth: CredentialsDto): ApiResult<AuthTokenDto>

    @POST("/signup")
    suspend fun registration(@Body user: CreateUserDto): ApiResult<NewUserDto>

    @POST("/password_reset")
    suspend fun resetPassword(@Body email: ResetPasswordDto): ApiResult<Unit>

    @PUT("/password/{token}")
    suspend fun changePassword(
        @Path("token") resetToken: String,
        @Body newPassword: NewPasswordDto
    ): ApiResult<Unit>

    @PUT("/profile/password")
    suspend fun updatePassword(
        @Header(AUTH_HEADER) authHeader: String,
        @Body newPassword: NewPasswordDto
    ): ApiResult<Unit>
}
