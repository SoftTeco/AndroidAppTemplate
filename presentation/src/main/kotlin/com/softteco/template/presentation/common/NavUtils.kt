package com.softteco.template.presentation.common

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

/**
 * It can happen that [NavController.navigate] is called twice. The second call can potentially fail if the internal
 * representation of the nav graph already moved to the destination. Hence, we simply ignore the second call.
 */

@Suppress("SwallowedException")
fun NavController.navigateSafe(directions: NavDirections) {
    try {
        this.navigate(directions)
    } catch (e: IllegalStateException) {
        // no-op
    } catch (e: IllegalArgumentException) {
        // no-op
    }
}

@Suppress("SwallowedException")
fun NavController.navigateSafe(directions: NavDirections, navOptions: NavOptions?) {
    try {
        this.navigate(directions, navOptions)
    } catch (e: IllegalStateException) {
        // no-op
    } catch (e: IllegalArgumentException) {
        // no-op
    }
}

@Suppress("SwallowedException")
fun NavController.navigateSafe(directions: NavDirections, extras: Navigator.Extras) {
    try {
        this.navigate(directions, extras)
    } catch (e: IllegalStateException) {
        // no-op
    } catch (e: IllegalArgumentException) {
        // no-op
    }
}

@Suppress("SwallowedException")
fun NavController.navigateSafe(id: Int) {
    try {
        this.navigate(id)
    } catch (e: IllegalStateException) {
        // no-op
    } catch (e: IllegalArgumentException) {
        // no-op
    }
}

fun Fragment.popOrFinish() {
    val navController = findNavController()
    if (!navController.popBackStack() || navController.currentDestination == null) {
        activity?.finish()
    }
}

fun Fragment.popOrFinish(upto: Int, inclusive: Boolean = false) {
    val navController = findNavController()
    if (!navController.popBackStack(upto, inclusive) || navController.currentDestination == null) {
        activity?.finish()
    }
}
