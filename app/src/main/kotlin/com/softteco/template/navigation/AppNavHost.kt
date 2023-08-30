package com.softteco.template.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.softteco.template.ui.feature.apisample.ApiSampleScreen
import com.softteco.template.ui.feature.home.HomeScreen
import com.softteco.template.ui.feature.login.LoginScreen
import com.softteco.template.ui.feature.profile.ProfileScreen
import com.softteco.template.ui.feature.settings.SettingsScreen
import com.softteco.template.ui.feature.signature.SignatureScreen

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
		modifier = modifier.padding(paddingValues = paddingValues)
	) {
		bottomBarGraph(navController)
		homeGraph(navController)
		profileGraph(navController)
		settingsGraph(navController)
	}
}

fun NavGraphBuilder.bottomBarGraph(navController: NavController) {
	navigation(
		startDestination = Screen.Home.route,
		route = Graph.BottomBar.route
	) {
		composable(Screen.Home.route) {
			HomeScreen(
				onApiSampleClicked = { navController.navigate(Screen.ApiSample.route) },
				onGoLoggingClicked = { navController.navigate(Screen.Login.route) }
			)
		}
		composable(Screen.Profile.route) {
			ProfileScreen(
				onSignatureClicked = { navController.navigate(Screen.Signature.route) }
			)
		}
		composable(Screen.Settings.route) {
			SettingsScreen()
		}
		composable(Screen.Login.route) {
			LoginScreen(onBackClicked = { navController.navigateUp() }, onLoginClicked = {})
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
