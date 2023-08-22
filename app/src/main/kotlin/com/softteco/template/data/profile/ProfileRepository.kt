package com.softteco.template.data.profile

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.entity.Profile

interface ProfileRepository {

    suspend fun getApi(): Result<String>

    suspend fun getUser(id: String): Result<Profile>
}
