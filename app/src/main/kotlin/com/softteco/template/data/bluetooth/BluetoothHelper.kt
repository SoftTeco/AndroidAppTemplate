package com.softteco.template.data.bluetooth

import android.bluetooth.BluetoothDevice
import com.softteco.template.MainActivity
import com.softteco.template.data.bluetooth.entity.DataLYWSD03MMC
import no.nordicsemi.android.support.v18.scanner.ScanResult

interface BluetoothHelper {

    fun init(activity: MainActivity)

    fun connectToDevice(bluetoothDevice: BluetoothDevice)

    fun disconnectFromDevice()

    fun registerReceiver()

    fun unregisterReceiver()

    fun provideOperation()

    fun provideOnScanCallback(onScanResult: (scanResult: ScanResult) -> Unit)

    fun provideOnConnectCallback(onConnect: () -> Unit)

    fun provideOnDisconnectCallback(onDisconnect: () -> Unit)

    fun provideOnDeviceResultCallback(onDeviceResult: (dataLYWSD03MMC: DataLYWSD03MMC) -> Unit)
}
