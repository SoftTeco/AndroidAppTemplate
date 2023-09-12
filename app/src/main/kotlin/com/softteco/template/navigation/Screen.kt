package com.softteco.template.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ApiSample : Screen("api_sample")

    object BLE : Screen("ble")

    object Profile : Screen("profile")
    object Signature : Screen("signature")

    object Settings : Screen("settings")
}
