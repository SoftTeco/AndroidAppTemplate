package com.softteco.template.data.source.remote

import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.domain.model.user.ForgotPasswordDto
import com.softteco.template.domain.model.user.LoginAuthDto
import com.softteco.template.domain.model.user.Response

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/login")
    suspend fun login(@Body userAuth: LoginAuthDto): Response<ResponseBody>

    @POST("/registration")
    suspend fun registration(@Body user: CreateUserDto): Response<ResponseBody>

    @POST("/forgotPassword")
    suspend fun restorePassword(@Body email: ForgotPasswordDto): Response<ResponseBody>
}