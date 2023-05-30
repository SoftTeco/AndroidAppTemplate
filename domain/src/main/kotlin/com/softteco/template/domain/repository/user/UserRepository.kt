package com.softteco.template.domain.repository.user

import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.domain.model.user.ForgotPasswordDto
import com.softteco.template.domain.model.user.LoginAuthDto
import com.softteco.template.domain.model.user.Response


typealias LoginResponse = Response<Boolean>
typealias RegisterResponse = Response<Boolean>
typealias RestorePasswordResponse = Response<Boolean>

interface UserRepository {

    suspend fun login(userAuth: LoginAuthDto): LoginResponse

    suspend fun registration(user: CreateUserDto): RegisterResponse

    suspend fun restorePassword(email: ForgotPasswordDto): RestorePasswordResponse
}