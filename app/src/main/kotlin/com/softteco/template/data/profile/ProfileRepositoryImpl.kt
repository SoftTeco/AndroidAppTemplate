package com.softteco.template.data.profile

import androidx.datastore.core.DataStore
import com.softteco.template.Constants.REQUEST_RETRY_DELAY
import com.softteco.template.data.RestCountriesApi
import com.softteco.template.data.TemplateApi
import com.softteco.template.data.base.ApiError
import com.softteco.template.data.base.ApiException
import com.softteco.template.data.base.ApiSuccess
import com.softteco.template.data.base.error.AppError
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.base.error.handleError
import com.softteco.template.data.base.map
import com.softteco.template.data.base.requestWithRetry
import com.softteco.template.data.profile.dto.AuthTokenDto
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.dto.NewPasswordDto
import com.softteco.template.data.profile.dto.ProfileDto
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.data.profile.dto.toModel
import com.softteco.template.data.profile.entity.AuthToken
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.data.profile.entity.Profile.Companion.toJson
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val templateApi: TemplateApi,
    private val authTokenEncryptedDataStore: DataStore<AuthTokenDto>,
    private val userProfileEncryptedDataStore: DataStore<ProfileDto>,
    private val countriesApi: RestCountriesApi,
) : ProfileRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getUser(): Result<Profile> {
        val token: String = authTokenEncryptedDataStore.data.first().toModel().token
        if (token.isEmpty()) {
            Timber.i("AuthToken not found")
            return Result.Error(AppError.LocalStorageAppError.AuthTokenNotFound)
        }

        val authToken = AuthToken(token)

        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            templateApi.getUser(authHeader = authToken.composeHeader())
        }

        return when (result) {
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

    @Suppress("TooGenericExceptionCaught")
    override suspend fun login(credentials: CredentialsDto): Result<AuthToken> {
        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            templateApi.login(credentials)
        }
        return when (result) {
            is ApiSuccess -> {
                val authToken = result.data.toModel()
                authTokenEncryptedDataStore.updateData { token -> token.copy(token = authToken.token) }
                Result.Success(authToken)
            }

            else -> handleError(result.map(AuthTokenDto::toModel))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun registration(user: CreateUserDto): Result<String> {
        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            templateApi.registration(user)
        }
        return when (result) {
            is ApiSuccess -> Result.Success(result.data.email)
            else -> handleError(result.map { it.toString() })
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun changePassword(
        resetToken: String,
        newPasswordDto: NewPasswordDto
    ): Result<Unit> {
        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            templateApi.changePassword(resetToken, newPasswordDto)
        }
        return when (result) {
            is ApiSuccess -> Result.Success(Unit)
            else -> handleError(result)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun resetPassword(email: ResetPasswordDto): Result<Unit> {
        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            templateApi.resetPassword(email)
        }
        return when (result) {
            is ApiSuccess -> Result.Success(Unit)
            else -> handleError(result)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun isUserLogin(): Result<Boolean> {
        return try {
            Result.Success(
                authTokenEncryptedDataStore.data.first().token.isNotEmpty()
            )
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(AppError.LocalStorageAppError.AuthTokenNotFound)
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
    override suspend fun logout(): Result<Unit> {
        return try {
            authTokenEncryptedDataStore.updateData { token -> token.copy(token = "") }
            Result.Success(Unit)
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
