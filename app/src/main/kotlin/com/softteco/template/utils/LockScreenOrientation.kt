package com.softteco.template.utils

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalContext.current.getActivity() ?: return
    val originalOrientation = activity.resources.configuration.orientation.run {
        if (this == ORIENTATION_LANDSCAPE) this else activity.requestedOrientation
    }

    DisposableEffect(orientation) {
        activity.requestedOrientation = orientation

        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}
