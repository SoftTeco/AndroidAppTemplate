package com.softteco.template.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Login : Screen("login")
    object SignUp : Screen("sign_up")
    object ResetPassword : Screen("reset_password")
    object ForgotPassword : Screen("forgot_password")
    object Bluetooth : Screen("bluetooth")
    object Chart : Screen("chart")
}
