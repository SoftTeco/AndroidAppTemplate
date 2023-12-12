package com.softteco.template.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.location.LocationManager
import com.softteco.template.MainActivity
import com.softteco.template.utils.PermissionType

interface BluetoothPermissionChecker {
    fun checkBluetoothSupport(
        bluetoothAdapter: BluetoothAdapter?,
        activity: MainActivity
    ): Boolean

    fun checkEnableDeviceModules(
        bluetoothAdapter: BluetoothAdapter?,
        locationManager: LocationManager?
    ): PermissionType

    fun hasPermissions(activity: MainActivity): Boolean
}
