package com.softteco.template.presentation.login.loginComponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(navController: NavHostController, title: String, showBackIcon: Boolean) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            if (showBackIcon && navController.previousBackStackEntry != null) {
                run {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        },
    )
}