package com.softteco.template.data.login

import com.softteco.template.data.login.model.LoginAuthDto
import com.softteco.template.data.base.error.Result

interface LoginRepository {
	suspend fun login(userAuth: LoginAuthDto): Result<Boolean>
}