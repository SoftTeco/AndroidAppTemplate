package com.softteco.template.data.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
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
import com.softteco.template.utils.getFromDataStore
import com.softteco.template.utils.saveToDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val templateApi: TemplateApi,
    private val errorHandler: ErrorHandler,
    private val dataStore: DataStore<Preferences>
) : ProfileRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getUser(): Result<Profile> {
        return try {
            val authTokenKey = stringPreferencesKey("auth_token")
            val token: String = dataStore.getFromDataStore(authTokenKey, "").first()
            if (token.isEmpty()) return Result.Error(ErrorEntity.Unknown)
            val authToken = AuthToken(token)
            Result.Success(templateApi.getUser(authHeader = authToken.composeHeader()).toModel())
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun login(credentials: CredentialsDto): Result<Unit> {
        return try {
            val authToken = templateApi.login(credentials).toModel()
            val authTokenKey = stringPreferencesKey("auth_token")
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
}
