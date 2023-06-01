package com.softteco.template.domain.usecase.account

data class UseCasesDb(
    val login: LoginDb,
    val register: RegistrationDb,
    val restorePassword: RestorePasswordDb
)