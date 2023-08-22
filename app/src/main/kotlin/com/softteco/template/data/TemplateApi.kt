package com.softteco.template.data

import com.softteco.template.data.profile.entity.Profile
import retrofit2.http.GET
import retrofit2.http.Path

interface TemplateApi {

    @GET("/api")
    suspend fun getApi(): String

    @GET("/api/user/{id}")
    suspend fun getUser(@Path("id") id: String): Profile
}
