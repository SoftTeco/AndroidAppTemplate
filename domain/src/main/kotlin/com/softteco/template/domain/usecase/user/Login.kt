package com.softteco.template.domain.usecase.user

import com.softteco.template.domain.model.user.LoginAuthDto
import com.softteco.template.domain.repository.user.UserRepository

class Login(private val repository: UserRepository) {
    suspend operator fun invoke(
        userAuth: LoginAuthDto
    ) = repository.login(
        userAuth
    )
}