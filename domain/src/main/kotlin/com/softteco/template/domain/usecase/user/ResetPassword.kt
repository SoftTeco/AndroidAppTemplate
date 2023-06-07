package com.softteco.template.domain.usecase.user

import com.softteco.template.domain.model.user.ResetPasswordDto
import com.softteco.template.domain.repository.user.UserRepository

class ResetPassword(private val repository: UserRepository) {
    suspend operator fun invoke(
        resetPassword: ResetPasswordDto
    ) = repository.resetPassword(
        resetPassword
    )
}