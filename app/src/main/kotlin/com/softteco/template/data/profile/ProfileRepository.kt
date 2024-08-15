package com.softteco.template.data.profile

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.dto.NewPasswordDto
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.data.profile.entity.AuthToken
import com.softteco.template.data.profile.entity.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<Result<Profile>>

    suspend fun login(credentials: CredentialsDto): Result<AuthToken>

    suspend fun registration(user: CreateUserDto): Result<String>

    suspend fun changePassword(resetToken: String, newPasswordDto: NewPasswordDto): Result<Unit>

    suspend fun resetPassword(email: ResetPasswordDto): Result<Unit>

    suspend fun isUserLogin(): Result<Boolean>

    suspend fun cacheProfile(profile: Profile): Result<Unit>

    suspend fun getCachedProfile(): Result<Profile>

    suspend fun logout(): Result<Unit>

    suspend fun getCountryList(name: String): Result<List<String>>
}
