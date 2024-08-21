package com.softteco.template.data.auth

import com.softteco.template.data.auth.dto.AuthTokenDto
import com.softteco.template.data.auth.dto.CreateUserDto
import com.softteco.template.data.auth.dto.NewPasswordDto
import com.softteco.template.data.auth.dto.ResetPasswordDto
import com.softteco.template.data.base.ApiResult
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.dto.NewUserDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {

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
}
