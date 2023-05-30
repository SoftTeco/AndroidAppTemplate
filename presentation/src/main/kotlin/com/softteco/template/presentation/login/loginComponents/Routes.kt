package com.softteco.template.presentation.login.loginComponents

sealed class Routes(val route: String) {
    object Login : Routes("Login")
    object Registration : Routes("Registration")
    object RestorePassword : Routes("RestorePassword")
}
