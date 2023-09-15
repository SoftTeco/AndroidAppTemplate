package com.softteco.template.data.profile

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.ForgotPasswordDto
import com.softteco.template.data.profile.dto.LoginAuthDto
import com.softteco.template.data.profile.entity.Profile

interface ProfileRepository {

    suspend fun getApi(): Result<String>

    suspend fun getUser(id: String): Result<Profile>

    suspend fun login(userAuth: LoginAuthDto): Result<String>

    suspend fun registration(user: CreateUserDto): Result<String>

    suspend fun restorePassword(email: ForgotPasswordDto): Result<String>
}
