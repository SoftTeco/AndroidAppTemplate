package com.softteco.template.data

import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.LoginAuthDto
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.data.profile.entity.Profile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TemplateApi {

    @GET("/api")
    suspend fun getApi(): String

    @GET("/api/user/{id}")
    suspend fun getUser(@Path("id") id: String): Profile

    @POST("/api/auth/login")
    suspend fun login(@Body userAuth: LoginAuthDto): String

    @POST("/api/auth/registration")
    suspend fun registration(@Body user: CreateUserDto): String

    @PATCH("/api/auth/resetPassword")
    suspend fun resetPassword(@Body password: ResetPasswordDto): String
}
