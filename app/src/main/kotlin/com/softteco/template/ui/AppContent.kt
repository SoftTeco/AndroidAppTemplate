package com.softteco.template.ui

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.softteco.template.navigation.AppBottomBar
import com.softteco.template.navigation.AppNavHost
import com.softteco.template.ui.components.RequestNotificationPermissionDialog
import com.softteco.template.ui.components.dialog.AppDialog
import com.softteco.template.ui.components.dialog.DialogController
import com.softteco.template.ui.components.snackbar.SnackbarController

@Composable
fun AppContent(
    startDestination: String,
    snackbarController: SnackbarController,
    dialogController: DialogController,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestNotificationPermissionDialog()
    }

    Box {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                AppBottomBar(navController = navController)
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                startDestination = startDestination,
                paddingValues = paddingValues
            )
        }

        val dialogs by dialogController.dialogs.collectAsState()
        key(dialogs) {
            dialogs.firstOrNull()?.let { dialogState ->
                AppDialog(
                    onDismissRequest = { dialogController.dismissDialog() },
                    state = dialogState
                )
            }
        }

        val snackbars by snackbarController.snackbars.collectAsState()
        LaunchedEffect(snackbars) {
            snackbars.firstOrNull()?.run {
                snackbarHostState.showSnackbar(
                    message = context.getString(messageRes),
                    actionLabel = actionLabelRes?.let { context.getString(it) },
                    withDismissAction = withDismissAction,
                    duration = duration
                )
                snackbarController.dismissSnackbar(this@run)
            }
        }
    }
}
