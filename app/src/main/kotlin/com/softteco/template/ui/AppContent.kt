package com.softteco.template.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.softteco.template.navigation.AppBottomBar
import com.softteco.template.navigation.AppNavHost

@Composable
fun AppContent(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            paddingValues = paddingValues
        )
    }
}
