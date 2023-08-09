package com.softteco.template.presentation.login.loginComponents.registration

data class PasswordValidationState(
    val hasMinimum: Boolean = false,
    val hasCapitalizedLetter: Boolean = false,
    val successful: Boolean = false
)
