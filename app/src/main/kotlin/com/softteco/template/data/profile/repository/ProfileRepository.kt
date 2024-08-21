package com.softteco.template.data.profile.repository

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.UpdateUserDto
import com.softteco.template.data.profile.entity.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<Result<Profile>>

    suspend fun cacheProfile(profile: Profile): Result<Unit>

    suspend fun getCachedProfile(): Result<Profile>

    suspend fun updateUser(updateUserDto: UpdateUserDto): Result<ProfileDto>

    suspend fun getCountryList(name: String): Result<List<String>>
}
