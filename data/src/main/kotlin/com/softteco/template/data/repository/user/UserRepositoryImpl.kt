package com.softteco.template.data.repository.user

import com.softteco.template.data.source.remote.UserApiService
import com.softteco.template.domain.model.user.*
import com.softteco.template.domain.repository.user.LoginResponse
import com.softteco.template.domain.repository.user.RegisterResponse
import com.softteco.template.domain.repository.user.RestorePasswordResponse
import com.softteco.template.domain.repository.user.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService
) : UserRepository {

    override suspend fun login(userAuth: LoginAuthDto): LoginResponse {
        return try {
            apiService.login(userAuth)
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Failure(e)
        }
    }

    override suspend fun registration(
        user: CreateUserDto
    ): RegisterResponse {
        return try {
            apiService.registration(
                user
            )
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Failure(e)
        }
    }

    override suspend fun restorePassword(email: ForgotPasswordDto): RestorePasswordResponse {
        return try {
            apiService.restorePassword(
                email
            )
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Failure(e)
        }
    }

    override suspend fun resetPassword(resetPassword: ResetPasswordDto): RestorePasswordResponse {
        return try {
            apiService.resetPassword(
                resetPassword
            )
            ApiResponse.Success(true)
        } catch (e: Exception) {
            ApiResponse.Failure(e)
        }
    }
}