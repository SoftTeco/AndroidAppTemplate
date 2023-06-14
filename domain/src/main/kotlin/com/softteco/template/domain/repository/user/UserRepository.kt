package com.softteco.template.domain.repository.user

import com.softteco.template.domain.model.user.*


typealias LoginResponse = ApiResponse<Boolean>
typealias RegisterResponse = ApiResponse<Boolean>
typealias RestorePasswordResponse = ApiResponse<Boolean>
typealias ResetPasswordResponse = ApiResponse<Boolean>

interface UserRepository {
    suspend fun login(userAuth: LoginAuthDto): LoginResponse

    suspend fun registration(user: CreateUserDto): RegisterResponse

    suspend fun restorePassword(email: ForgotPasswordDto): RestorePasswordResponse

    suspend fun resetPassword(resetPassword: ResetPasswordDto): RestorePasswordResponse
}