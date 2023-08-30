package com.softteco.template.data.login

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.UserApi
import com.softteco.template.data.base.error.ErrorHandler
import com.softteco.template.data.login.model.LoginAuthDto
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
	private val userApi: UserApi,
	private val errorHandler: ErrorHandler,
) : LoginRepository {
	override suspend fun login(userAuth: LoginAuthDto): Result<String> {
		return try {
			Result.Success(userApi.login(userAuth))

		} catch (e: Exception) {
			Result.Error(errorHandler.getError(e))
		}
	}
}