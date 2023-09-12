package com.softteco.template.ui.feature.bluetooth.modules

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi

const val REQUEST_ALLOW_BT = 1

val PERMISSIONS_FOR_BLUETOOTH_BEFORE_ANDROID_12 = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@RequiresApi(Build.VERSION_CODES.S)
val PERMISSIONS_FOR_BLUETOOTH_AFTER_ANDROID_12 = arrayOf(
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.BLUETOOTH_ADVERTISE
)

enum class PermissionType {
    BLUETOOTH_TURNED_OFF, LOCATION_TURNED_OFF, BLUETOOTH_AND_LOCATION_TURNED_ON
}

class BluetoothPermissionChecker (
    private val activity: Activity?,
    private val bluetoothAdapter: BluetoothAdapter?,
    private val locationManager: LocationManager?
) {
    fun checkBluetoothSupport(): Boolean {
        return when {
            bluetoothAdapter == null -> false
            !activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)!! -> false
            else -> true
        }
    }

    fun checkEnableDeviceModules(): PermissionType {
        var permissionType = PermissionType.BLUETOOTH_AND_LOCATION_TURNED_ON
        locationManager?.let {
            if (!it.isProviderEnabled(LocationManager.GPS_PROVIDER) && !it.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            ) {
                permissionType = PermissionType.LOCATION_TURNED_OFF
            }
        }
        bluetoothAdapter?.let { if (!it.isEnabled) return PermissionType.BLUETOOTH_TURNED_OFF }
        return permissionType
    }

    fun hasPermissions(): Boolean {
        var hasPermission = true
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                PERMISSIONS_FOR_BLUETOOTH_AFTER_ANDROID_12.forEach {
                    if (!requestPermission(it, PERMISSIONS_FOR_BLUETOOTH_AFTER_ANDROID_12)) hasPermission = false
                }
            }
            else -> {
                PERMISSIONS_FOR_BLUETOOTH_BEFORE_ANDROID_12.forEach {
                    if (!requestPermission(it, PERMISSIONS_FOR_BLUETOOTH_BEFORE_ANDROID_12)) hasPermission = false
                }
            }
        }
        return hasPermission
    }

    private fun requestPermission(permissionValue: String, permissions: Array<String>): Boolean {
        return when {
            activity?.checkSelfPermission(permissionValue) != PackageManager.PERMISSION_GRANTED -> {
                activity?.requestPermissions(
                    permissions,
                    REQUEST_ALLOW_BT
                )
                false
            }
            activity.checkSelfPermission(permissionValue) == PackageManager.PERMISSION_DENIED -> false
            else -> true
        }
    }
}
