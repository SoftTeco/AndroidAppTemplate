package com.softteco.template.data.source.remote

import com.softteco.template.domain.model.user.*

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    suspend fun login(@Body userAuth: LoginAuthDto): Response<ResponseBody>

    @POST("/registration")
    suspend fun registration(@Body user: CreateUserDto): Response<ResponseBody>

    @POST("/forgotPassword")
    suspend fun restorePassword(@Body email: ForgotPasswordDto): Response<ResponseBody>

    @PATCH("/resetPassword")
    suspend fun resetPassword(@Body resetPasswordDto: ResetPasswordDto): Response<ResponseBody>
}