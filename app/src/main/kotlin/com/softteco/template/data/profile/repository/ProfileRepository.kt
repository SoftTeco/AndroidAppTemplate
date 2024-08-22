package com.softteco.template.data.profile.repository

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.entity.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUser(): Flow<Result<Profile>>

    suspend fun cacheProfile(profile: Profile): Result<Unit>

    suspend fun getCachedProfile(): Result<Profile>

    suspend fun getCountryList(name: String): Result<List<String>>
}
