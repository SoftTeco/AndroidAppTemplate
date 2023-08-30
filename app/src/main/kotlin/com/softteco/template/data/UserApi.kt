package com.softteco.template.data

import com.softteco.template.data.login.model.CreateUserDto
import com.softteco.template.data.login.model.LoginAuthDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/auth/login")
    suspend fun login(@Body userAuth: LoginAuthDto): Boolean

    @POST("/auth/registration")
    suspend fun registration(@Body user: CreateUserDto): Boolean
}
