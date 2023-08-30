package com.softteco.template.data.login

import com.softteco.template.data.base.error.Result
import com.softteco.template.data.login.model.CreateUserDto

interface CreateUserRepository {
	suspend fun registration(user: CreateUserDto): Result<Boolean>
}