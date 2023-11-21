package com.softteco.template.utils

fun checkDeviceConnection(macAddress: String) =
    if (BluetoothHelper.connectedDevices.containsKey(macAddress))
        BluetoothHelper.connectedDevices[macAddress] else false
