package com.softteco.template.domain.repository.user

import com.softteco.template.domain.model.user.*


typealias LoginResponse = Response<Boolean>
typealias RegisterResponse = Response<Boolean>
typealias RestorePasswordResponse = Response<Boolean>
typealias ResetPasswordResponse = Response<Boolean>

interface UserRepository {
    suspend fun login(userAuth: LoginAuthDto): LoginResponse

    suspend fun registration(user: CreateUserDto): RegisterResponse

    suspend fun restorePassword(email: ForgotPasswordDto): RestorePasswordResponse

    suspend fun resetPassword(resetPassword: ResetPasswordDto): RestorePasswordResponse
}