package com.softteco.template.domain.usecase.user

import com.softteco.template.domain.model.user.ForgotPasswordDto
import com.softteco.template.domain.repository.user.UserRepository

class RestorePassword(private val repository: UserRepository) {
    suspend operator fun invoke(
        email: ForgotPasswordDto
    ) = repository.restorePassword(
        email
    )
}