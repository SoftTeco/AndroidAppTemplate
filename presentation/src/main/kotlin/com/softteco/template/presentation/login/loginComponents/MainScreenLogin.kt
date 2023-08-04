package com.softteco.template.presentation.login.loginComponents

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.softteco.template.presentation.R
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

        dialog(    //TODO
            route = "exit_dialog",
            dialogProperties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ){  AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    navController.navigate(Routes.Login.route)
                }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },

            text = {
                Text(text = "message")

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(5.dp),
            containerColor = Color.White
        )
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