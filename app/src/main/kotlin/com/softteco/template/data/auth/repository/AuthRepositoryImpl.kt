package com.softteco.template.data.auth.repository

import androidx.datastore.core.DataStore
import com.softteco.template.Constants.REQUEST_RETRY_DELAY
import com.softteco.template.data.auth.AuthApi
import com.softteco.template.data.auth.dto.AuthTokenDto
import com.softteco.template.data.auth.dto.CreateUserDto
import com.softteco.template.data.auth.dto.NewPasswordDto
import com.softteco.template.data.auth.dto.ResetPasswordDto
import com.softteco.template.data.auth.dto.toModel
import com.softteco.template.data.auth.entity.AuthToken
import com.softteco.template.data.base.ApiSuccess
import com.softteco.template.data.base.error.AppError
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.base.error.handleError
import com.softteco.template.data.base.map
import com.softteco.template.data.base.requestWithRetry
import com.softteco.template.data.profile.dto.CredentialsDto
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authTokenEncryptedDataStore: DataStore<AuthTokenDto>,
) : AuthRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun login(credentials: CredentialsDto): Result<AuthToken> {
        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            authApi.login(credentials)
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
            authApi.registration(user)
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
            authApi.changePassword(resetToken, newPasswordDto)
        }
        return when (result) {
            is ApiSuccess -> Result.Success(Unit)
            else -> handleError(result)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun resetPassword(email: ResetPasswordDto): Result<Unit> {
        val result = requestWithRetry(delay = REQUEST_RETRY_DELAY) {
            authApi.resetPassword(email)
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
    override suspend fun logout(): Result<Unit> {
        return try {
            authTokenEncryptedDataStore.updateData { token -> token.copy(token = "") }
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(AppError.UnknownError())
        }
    }
}
