package com.softteco.template.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalContext.current.getActivity() ?: return
    val originalOrientation = activity.resources.configuration.orientation

    DisposableEffect(orientation) {
        activity.requestedOrientation = orientation

        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}
