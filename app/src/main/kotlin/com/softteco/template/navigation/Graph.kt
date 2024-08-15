package com.softteco.template.navigation

sealed class Graph(val route: String) {
    object Main : Graph("bottom_bar_graph")
    object Login : Graph("login_graph")
}
