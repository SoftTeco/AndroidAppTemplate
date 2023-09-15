package com.softteco.template.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ApiSample : Screen("api_sample")

    object Profile : Screen("profile")
    object Signature : Screen("signature")

    object Settings : Screen("settings")

    object Login : Screen("login")

    object SignUp : Screen("sign_up")

    object ForgotPassword : Screen("forgot_password")
}
