package com.softteco.template.presentation.login.loginComponents

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

        composable(Routes.RestorePassword.route) { navBackStack ->
            RestorePasswordScreen(navController = navController)
        }

        composable(Routes.ResetPassword.route) { navBackStack ->
            ResetPasswordScreen(navController = navController)
        }
    }
}