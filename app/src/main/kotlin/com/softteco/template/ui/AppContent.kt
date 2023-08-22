package com.softteco.template.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.softteco.template.navigation.AppBottomBar
import com.softteco.template.navigation.AppNavHost
import com.softteco.template.navigation.Graph

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            startDestination = Graph.BottomBar.route,
            paddingValues = paddingValues
        )
    }
}
