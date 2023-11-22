package com.softteco.template.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    when (navBackStackEntry?.destination?.route) {
        Screen.Profile.route, Screen.Home.route, Screen.Settings.route -> {
            bottomBarState.value = true
        }

        else -> bottomBarState.value = false
    }
    AnimatedVisibility(
        modifier = modifier,
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            NavigationBar {
                BottomNavigationItem::class.sealedSubclasses.mapNotNull { it.objectInstance }
                    .forEachIndexed { _, navigationItem ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == navigationItem.route } == true
                        NavigationBarItem(
                            selected = isSelected,
                            label = {
                                Text(stringResource(id = navigationItem.textId))
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) {
                                        navigationItem.selectedIcon
                                    } else {
                                        navigationItem.unSelectedIcon
                                    },
                                    contentDescription = stringResource(id = navigationItem.textId)
                                )
                            },
                            onClick = {
                                navController.navigate(navigationItem.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
            }
        }
    )
}
