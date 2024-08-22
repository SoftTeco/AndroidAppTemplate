package com.softteco.template.data.profile.repository

import androidx.datastore.core.DataStore
import com.softteco.template.Constants.REQUEST_RETRY_DELAY
import com.softteco.template.data.auth.dto.AuthTokenDto
import com.softteco.template.data.auth.dto.toModel
import com.softteco.template.data.auth.entity.AuthToken
import com.softteco.template.data.base.ApiError
import com.softteco.template.data.base.ApiException
import com.softteco.template.data.base.ApiSuccess
import com.softteco.template.data.base.error.AppError
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.base.requestWithRetry
import com.softteco.template.data.profile.ProfileApi
import com.softteco.template.data.profile.RestCountriesApi
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.toModel
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.data.profile.entity.Profile.Companion.toJson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi,
    private val authTokenEncryptedDataStore: DataStore<AuthTokenDto>,
    private val userProfileEncryptedDataStore: DataStore<ProfileDto>,
    private val countriesApi: RestCountriesApi,
) : ProfileRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Suppress("TooGenericExceptionCaught")
    override fun getUser(): Flow<Result<Profile>> = authTokenEncryptedDataStore.data.mapLatest {
        val token: String = it.toModel().token
        if (token.isEmpty()) {
            Timber.i("AuthToken not found")
            Result.Error(AppError.LocalStorageAppError.AuthTokenNotFound)
        } else {
            val authToken = AuthToken(token)

            val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
                profileApi.getUser(authHeader = authToken.composeHeader())
            }

            when (result) {
                is ApiSuccess -> {
                    var profile = result.data.toModel()
                    val cachedProfile = getCachedProfile()
                    if (cachedProfile is Result.Success) {
                        if (cachedProfile.data.username == profile.username) {
                            profile = cachedProfile.data
                        }
                    }
                    Result.Success(profile)
                }

                is ApiError -> Result.Error(AppError.AuthError.findByCode(result.errorBody?.code))

                is ApiException -> Result.Error(AppError.NetworkError())
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun cacheProfile(profile: Profile): Result<Unit> {
        return try {
            userProfileEncryptedDataStore.updateData { profileDto ->
                profileDto.copy(
                    id = profile.id,
                    username = profile.username,
                    email = profile.email,
                    createdAt = profile.createdAt,
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    birthDate = profile.birthDate,
                    country = profile.country
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(AppError.UnknownError())
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getCachedProfile(): Result<Profile> {
        return try {
            val profileJson = userProfileEncryptedDataStore.data.first().toModel().toJson()
            if (profileJson.isEmpty()) {
                Timber.i("Profile is empty")
                return Result.Error(AppError.UnknownError())
            }
            val profile = Profile.fromJson(profileJson)
            Result.Success(profile)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(AppError.UnknownError())
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getCountryList(name: String): Result<List<String>> {
        return try {
            val countries = countriesApi.getCountryList(name).map { it.name.common }
            Result.Success(countries)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(AppError.UnknownError())
        }
    }
}
