package com.softteco.template.data.login

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.login.model.LoginAuthDto

interface LoginRepository {
    suspend fun login(userAuth: LoginAuthDto): Result<Boolean>
}
