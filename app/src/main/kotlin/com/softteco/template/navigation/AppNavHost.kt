package com.softteco.template.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.softteco.template.navigation.AppNavHost.DEEP_LINK_URI
import com.softteco.template.navigation.AppNavHost.RESET_PASSWORD_PATH
import com.softteco.template.navigation.AppNavHost.RESET_TOKEN_ARG
import com.softteco.template.ui.feature.apisample.ApiSampleScreen
import com.softteco.template.ui.feature.bluetooth.BluetoothScreen
import com.softteco.template.ui.feature.forgotPassword.ForgotPasswordScreen
import com.softteco.template.ui.feature.home.HomeScreen
import com.softteco.template.ui.feature.login.LoginScreen
import com.softteco.template.ui.feature.profile.ProfileScreen
import com.softteco.template.ui.feature.resetPassword.ResetPasswordScreen
import com.softteco.template.ui.feature.settings.SettingsScreen
import com.softteco.template.ui.feature.signUp.SignUpScreen
import com.softteco.template.ui.feature.signature.SignatureScreen

object AppNavHost {
    const val DEEP_LINK_URI = "https://template.softteco.com.deep_link"
    const val RESET_PASSWORD_PATH = "reset_password"
    const val RESET_TOKEN_ARG = "token"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        bottomBarGraph(navController, Modifier.padding(paddingValues = paddingValues))
        homeGraph(navController)
        profileGraph(navController)
        settingsGraph(navController)
        loginGraph(navController)
    }
}

fun NavGraphBuilder.bottomBarGraph(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    navigation(
        startDestination = Screen.Home.route,
        route = Graph.BottomBar.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                modifier = modifier,
                onLoginClicked = { navController.navigate(Screen.Login.route) },
                onSignatureClicked = { },
                onBleClicked = { navController.navigate(Screen.Bluetooth.route) },
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                modifier,
                onSignatureClicked = { navController.navigate(Screen.Signature.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(modifier)
        }
    }
}

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Login.route,
        route = Graph.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccess = {
                    navController.navigate(
                        Screen.Profile.route,
                        NavOptions.Builder().setPopUpTo(Screen.Home.route, true).build()
                    )
                },
                onSignUpClicked = { navController.navigate(Screen.SignUp.route) },
                onForgotPasswordClicked = { navController.navigate(Screen.ForgotPassword.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccess = { navController.navigate(Screen.Login.route) }
            )
        }
        composable(
            route = Screen.ResetPassword.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$DEEP_LINK_URI/$RESET_PASSWORD_PATH/{$RESET_TOKEN_ARG}"
                }
            )
        ) {
            ResetPasswordScreen(
                onSuccess = { navController.navigate(Screen.Login.route) },
            )
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccess = { navController.navigate(Screen.Login.route) }
            )
        }
    }
}

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Home.route,
        route = Graph.Home.route
    ) {
        composable(Screen.ApiSample.route) {
            ApiSampleScreen(onBackClicked = { navController.popBackStack() })
        }
        composable(Screen.Bluetooth.route) {
            BluetoothScreen(
                onBackClicked = { navController.navigateUp() }
            )
        }
    }
}

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Profile.route,
        route = Graph.Profile.route
    ) {
        composable(Screen.Signature.route) {
            SignatureScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Settings.route,
        route = Graph.Settings.route
    ) {
        composable(Screen.Signature.route) {
            SignatureScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}
