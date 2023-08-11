package com.softteco.template.presentation.login.loginComponents

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDirections


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String, showBackIcon: Boolean
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
    TopAppBar(
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
        navigationIcon = {
            if (showBackIcon) {
                run {
                    IconButton(onClick = { dispatcher.onBackPressed() }) {
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