package com.softteco.template.ui.components

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.softteco.template.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) {
            RationaleDialog(
                titleRes = R.string.notification_permission_title,
                descriptionRes = R.string.notification_permission_description
            )
        } else {
            PermissionDialog(
                titleRes = R.string.notification_permission_title,
                descriptionRes = R.string.notification_permission_description,
            ) { permissionState.launchPermissionRequest() }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) {
            RationaleDialog(
                titleRes = R.string.location_permission_title,
                descriptionRes = R.string.location_permission_description
            )
        } else {
            PermissionDialog(
                titleRes = R.string.location_permission_title,
                descriptionRes = R.string.location_permission_description,
            ) { permissionState.launchPermissionRequest() }
        }
    }
}
