package com.softteco.template.data.profile

import com.softteco.template.data.TemplateApi
import com.softteco.template.data.base.error.ErrorHandler
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.dto.CreateUserDto
import com.softteco.template.data.profile.dto.LoginAuthDto
import com.softteco.template.data.profile.dto.ResetPasswordDto
import com.softteco.template.data.profile.entity.Profile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val templateApi: TemplateApi,
    private val errorHandler: ErrorHandler,
) : ProfileRepository {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getApi(): Result<String> {
        return try {
            Result.Success(templateApi.getApi())
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun getUser(id: String): Result<Profile> {
        return try {
            Result.Success(templateApi.getUser(id))
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun login(userAuth: LoginAuthDto): Result<String> {
        return try {
            Result.Success(templateApi.login(userAuth))
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun registration(user: CreateUserDto): Result<String> {
        return try {
            Result.Success(templateApi.registration(user))
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun resetPassword(resetPasswordDto: ResetPasswordDto): Result<String> {
        return try {
            Result.Success(templateApi.resetPassword(resetPasswordDto))
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }
}
