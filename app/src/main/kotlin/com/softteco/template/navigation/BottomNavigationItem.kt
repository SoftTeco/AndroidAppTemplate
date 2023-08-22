package com.softteco.template.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.softteco.template.R

sealed class BottomNavigationItem(
    val textId: Int = -1,
    val unSelectedIcon: ImageVector = Icons.Filled.Home,
    val selectedIcon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    object Home : BottomNavigationItem(
        textId = R.string.home,
        unSelectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = Screen.Home.route
    )

    object Profile : BottomNavigationItem(
        textId = R.string.profile,
        unSelectedIcon = Icons.Outlined.AccountCircle,
        selectedIcon = Icons.Filled.AccountCircle,
        route = Screen.Profile.route
    )

    object Settings : BottomNavigationItem(
        textId = R.string.settings,
        unSelectedIcon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        route = Screen.Settings.route
    )
}
