package com.softteco.template.ui

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.softteco.template.navigation.AppBottomBar
import com.softteco.template.navigation.AppNavHost
import com.softteco.template.ui.components.RequestNotificationPermissionDialog
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

@Composable
fun AppContent(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    LaunchedEffect(Unit) {
        launch {
            val token = Firebase.messaging.token.await()
            Timber.tag("FCM token:").d(token)
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }
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
