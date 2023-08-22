package com.softteco.template.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softteco.template.ui.feature.home.HomeScreen
import com.softteco.template.ui.feature.profile.ProfileScreen

@Composable
fun AppContent() {
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}

@Composable
private fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destination.Home.route) {
        composable(Destination.Home.route) {
            HomeScreen(onProfileClick = {
                navController.navigate(Destination.Profile.route)
            })
        }
        composable(Destination.Profile.route) {
            ProfileScreen(onBackClick = { navController.navigateUp() })
        }
    }
}

sealed class Destination(val route: String) {
    object Home : Destination("home")
    object Profile : Destination("profile")
}
