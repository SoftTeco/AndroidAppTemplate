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
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun registration(
        user: CreateUserDto
    ): RegisterResponse {
        return try {
            apiService.registration(
                user
            )
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun restorePassword(email: ForgotPasswordDto): RestorePasswordResponse {
        return try {
            apiService.restorePassword(
                email
            )
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun resetPassword(resetPassword: ResetPasswordDto): RestorePasswordResponse {
        return try {
            apiService.resetPassword(
                resetPassword
            )
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}