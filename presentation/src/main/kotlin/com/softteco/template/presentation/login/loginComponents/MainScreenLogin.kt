package com.softteco.template.presentation.login.loginComponents

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.softteco.template.presentation.login.loginComponents.login.LoginScreen
import com.softteco.template.presentation.login.loginComponents.password.RestorePasswordScreen
import com.softteco.template.presentation.login.loginComponents.registration.RegistrationScreen
import com.softteco.template.presentation.login.loginComponents.reset.ResetPasswordScreen

@Composable
fun MainScreenLogin() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Login.route) {

        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Routes.Registration.route) {
            RegistrationScreen(navController = navController)
        }

        composable(Routes.RestorePassword.route) {
            RestorePasswordScreen(navController = navController)
        }

        composable(Routes.ResetPassword.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "host/api/user/reset/{token}"
                    action = Intent.ACTION_VIEW
                }
            ), arguments = listOf(
                navArgument("token") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) { navBackStack ->
            val token = navBackStack.arguments?.getString("token")
            ResetPasswordScreen(navController = navController, token = token!!)
        }
    }
}