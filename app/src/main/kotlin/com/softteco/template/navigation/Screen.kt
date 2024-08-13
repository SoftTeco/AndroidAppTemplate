package com.softteco.template.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Login : Screen("login")
    data object SignUp : Screen("sign_up")
    data object ResetPassword : Screen("reset_password")
    data object ForgotPassword : Screen("forgot_password")
    data object OpenSourceLicenses : Screen("open_source_licenses")
    data object Navigation : Screen("navigation")
}
