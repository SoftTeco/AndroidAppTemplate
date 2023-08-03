package com.softteco.template.domain.usecase.user


data class UseCases(
    val login: Login,
    val register: Registration,
    val restorePassword: RestorePassword,
    val resetPassword: ResetPassword
)

data class CountryUseCases(
    val country: Country
)
