package com.softteco.template.domain.usecase.user

import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.domain.repository.user.UserRepository

class Registration(private val repository: UserRepository) {
    suspend operator fun invoke(
        user: CreateUserDto
    ) = repository.registration(
        user
    )
}