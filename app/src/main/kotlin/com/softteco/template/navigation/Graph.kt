package com.softteco.template.navigation

sealed class Graph(val route: String) {
    object Home : Graph("home_graph")
    object Profile : Graph("profile_graph")
    object Settings : Graph("settings_graph")
    object BottomBar : Graph("bottom_bar_graph")
    object Login : Graph("login_graph")
    object Bluetooth : Graph("bluetooth_graph")
}
