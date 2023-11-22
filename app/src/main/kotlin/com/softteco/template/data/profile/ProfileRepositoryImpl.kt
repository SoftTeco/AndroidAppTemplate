package com.softteco.template.data.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.softteco.template.data.RestCountriesApi
import com.softteco.template.data.TemplateApi
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.ErrorHandler
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.CredentialsDto
import com.softteco.template.data.profile.dto.NewPasswordDto
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.data.profile.dto.toModel
import com.softteco.template.data.profile.entity.AuthToken
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.data.profile.entity.Profile.Companion.toJson
import com.softteco.template.utils.getFromDataStore
import com.softteco.template.utils.saveToDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private const val AUTH_TOKEN_KEY = "auth_token"
private const val PROFILE_KEY = "profile"

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val templateApi: TemplateApi,
    private val errorHandler: ErrorHandler,
    private val dataStore: DataStore<Preferences>,
    private val countriesApi: RestCountriesApi,
) : ProfileRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getUser(): Result<Profile> {
        return try {
            val authTokenKey = stringPreferencesKey(AUTH_TOKEN_KEY)
            val token: String = dataStore.getFromDataStore(authTokenKey, "").first()
            if (token.isEmpty()) return Result.Error(ErrorEntity.Unknown)
            val authToken = AuthToken(token)
            var profile = templateApi.getUser(authHeader = authToken.composeHeader()).toModel()
            val cachedProfile = getCachedProfile()
            if (cachedProfile is Result.Success) {
                if (cachedProfile.data.username == profile.username) {
                    profile = cachedProfile.data
                }
            }
            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun login(credentials: CredentialsDto): Result<Unit> {
        return try {
            val authToken = templateApi.login(credentials).toModel()
            val authTokenKey = stringPreferencesKey(AUTH_TOKEN_KEY)
            dataStore.saveToDataStore(authTokenKey, authToken.token)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun registration(user: CreateUserDto): Result<String> {
        return try {
            val newUser = templateApi.registration(user)
            Result.Success(newUser.email)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun changePassword(
        resetToken: String,
        newPasswordDto: NewPasswordDto
    ): Result<Unit> {
        return try {
            templateApi.changePassword(resetToken, newPasswordDto)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun resetPassword(email: ResetPasswordDto): Result<Unit> {
        return try {
            templateApi.resetPassword(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun isUserLogin(): Result<Boolean> {
        return try {
            Result.Success(
                dataStore.getFromDataStore(stringPreferencesKey(AUTH_TOKEN_KEY), "").first()
                    .isNotEmpty()
            )
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun cacheProfile(profile: Profile): Result<Unit> {
        return try {
            val profileKey = stringPreferencesKey(PROFILE_KEY)
            dataStore.saveToDataStore(profileKey, profile.toJson())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getCachedProfile(): Result<Profile> {
        return try {
            val profileKey = stringPreferencesKey(PROFILE_KEY)
            val profileJson = dataStore.getFromDataStore(profileKey, "").first()
            if (profileJson.isEmpty()) return Result.Error(ErrorEntity.Unknown)
            val profile = Profile.fromJson(profileJson)
            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun logout(): Result<Unit> {
        return try {
            val authTokenKey = stringPreferencesKey(AUTH_TOKEN_KEY)
            dataStore.edit { it.remove(authTokenKey) }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getCountryList(name: String): Result<List<String>> {
        return try {
            val countries = countriesApi.getCountryList(name).map { it.name.common }
            Result.Success(countries)
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }
}
