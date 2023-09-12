package com.softteco.template.ui.feature.bluetooth.modules

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import com.softteco.template.BuildConfig.BLUETOOTH_CHARACTERISTIC_FOR_WRITE_UUID_VALUE
import com.softteco.template.BuildConfig.BLUETOOTH_SERVICE_UUID_VALUE
import java.util.UUID

private const val TIMEOUT_ADVERTISING = 0

class BluetoothServer (
    private var bluetoothManager: BluetoothManager,
    private var context: Context?,
    private var onReceived: ((ByteArray) -> Unit)? = null
) {

    private var bluetoothGattServer: BluetoothGattServer? = null

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {}

        override fun onStartFailure(errorCode: Int) {}
    }

    @SuppressLint("MissingPermission")
    fun startServer() {
        val callback: BluetoothGattServerCallback = object : BluetoothGattServerCallback() {
            override fun onConnectionStateChange(
                device: BluetoothDevice,
                status: Int,
                newState: Int
            ) {
                super.onConnectionStateChange(device, status, newState)
            }

            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                super.onCharacteristicWriteRequest(
                    device,
                    requestId,
                    characteristic,
                    preparedWrite,
                    responseNeeded,
                    offset,
                    value
                )
                onReceived?.invoke(value)
            }
        }

        bluetoothGattServer = bluetoothManager.openGattServer(context, callback)
        val bluetoothGattServerService = BluetoothGattService(
            UUID.fromString(BLUETOOTH_SERVICE_UUID_VALUE),
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )
        val characteristic = BluetoothGattCharacteristic(
            UUID.fromString(BLUETOOTH_CHARACTERISTIC_FOR_WRITE_UUID_VALUE),
            BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        bluetoothGattServerService.addCharacteristic(characteristic)
        bluetoothGattServer?.addService(bluetoothGattServerService)
    }

    @SuppressLint("MissingPermission")
    fun stopServer() {
        bluetoothGattServer?.close()
    }

    @SuppressLint("MissingPermission")
    fun startAdvertising() {
        val bluetoothLeAdvertiser: BluetoothLeAdvertiser? =
            bluetoothManager.adapter.bluetoothLeAdvertiser

        bluetoothLeAdvertiser?.let {
            val settings = AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(TIMEOUT_ADVERTISING)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build()

            val data = AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .addServiceUuid(ParcelUuid.fromString(BLUETOOTH_SERVICE_UUID_VALUE))
                .build()

            it.startAdvertising(settings, data, advertiseCallback)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopAdvertising() {
        val bluetoothLeAdvertiser: BluetoothLeAdvertiser? =
            bluetoothManager.adapter.bluetoothLeAdvertiser
        bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
    }
}
