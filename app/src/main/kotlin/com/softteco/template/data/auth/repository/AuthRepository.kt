package com.softteco.template.data.auth.repository

import com.softteco.template.data.auth.dto.CreateUserDto
import com.softteco.template.data.auth.dto.NewPasswordDto
import com.softteco.template.data.auth.dto.ResetPasswordDto
import com.softteco.template.data.auth.entity.AuthToken
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.dto.CredentialsDto

interface AuthRepository {

    suspend fun login(credentials: CredentialsDto): Result<AuthToken>

    suspend fun registration(user: CreateUserDto): Result<String>

    suspend fun changePassword(resetToken: String, newPasswordDto: NewPasswordDto): Result<Unit>

    suspend fun resetPassword(email: ResetPasswordDto): Result<Unit>

    suspend fun isUserLogin(): Result<Boolean>

    suspend fun logout(): Result<Unit>
}
