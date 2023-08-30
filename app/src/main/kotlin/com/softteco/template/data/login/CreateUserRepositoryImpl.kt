package com.softteco.template.data.login

import com.softteco.template.data.UserApi
import com.softteco.template.data.base.error.ErrorHandler
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.login.model.CreateUserDto
import javax.inject.Inject

class CreateUserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val errorHandler: ErrorHandler,
) : CreateUserRepository {

    override suspend fun registration(user: CreateUserDto): Result<Boolean> {
        return try {
            Result.Success(userApi.registration(user))
        } catch (e: Exception) {
            Result.Error(errorHandler.getError(e))
        }
    }
}
