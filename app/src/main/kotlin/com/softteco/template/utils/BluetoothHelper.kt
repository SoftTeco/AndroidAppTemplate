package com.softteco.template.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.softteco.template.MainActivity
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult

object BluetoothHelper {
    private lateinit var bluetoothReceiver: BroadcastReceiver
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var locationManager: LocationManager
    private var pairedDevices = hashMapOf<String, BluetoothGatt>()

    @Volatile var connectedDevices = hashMapOf<String, Boolean>()
    private lateinit var resultBluetoothEnableLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultLocationEnableLauncher: ActivityResultLauncher<Intent>
    var onConnect: (() -> Unit)? = null
    var onDisconnect: (() -> Unit)? = null
    var onScanResult: ((scanResult: ScanResult) -> Unit)? = null

    private val scanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(
            callbackType: Int,
            scanResult: ScanResult
        ) {
            super.onScanResult(callbackType, scanResult)
            scanResult.device.name?.let {
                onScanResult?.invoke(scanResult)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun initBluetoothHelper(activity: MainActivity) {
        resultBluetoothEnableLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
        resultLocationEnableLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (
                    intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.STATE_OFF
                    )
                ) {
                    BluetoothAdapter.STATE_ON -> {
                        provideBluetoothOperation(activity)
                    }

                    BluetoothAdapter.STATE_OFF -> {
                        stopScan()
                    }
                }
            }
        }
        bluetoothManager =
            activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    fun registerReceiver(activity: MainActivity) {
        activity.registerReceiver(
            bluetoothReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
    }

    fun unregisterReceiver(activity: MainActivity) {
        activity.unregisterReceiver(bluetoothReceiver)
    }

    private fun startScan() {
        stopScan()
        BluetoothLeScannerCompat.getScanner().startScan(scanCallback)
    }

    fun stopScan() {
        BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
    }

    fun provideBluetoothOperation(activity: MainActivity) {
        if (BluetoothPermissionChecker.checkBluetoothSupport(bluetoothAdapter, activity) &&
            BluetoothPermissionChecker.hasPermissions(activity)
        ) {
            when (
                BluetoothPermissionChecker.checkEnableDeviceModules(
                    bluetoothAdapter,
                    locationManager
                )
            ) {
                PermissionType.LOCATION_TURNED_OFF -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    resultLocationEnableLauncher.launch(intent)
                }

                PermissionType.BLUETOOTH_TURNED_OFF -> {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    resultBluetoothEnableLauncher.launch(intent)
                }

                PermissionType.BLUETOOTH_AND_LOCATION_TURNED_ON -> {
                    makeBluetoothOperation(activity)
                }
            }
        }
    }

    private fun makeBluetoothOperation(activity: MainActivity) {
        if (BluetoothPermissionChecker.hasPermissions(activity)) {
            startScan()
        }
    }

    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                pairedDevices[gatt.device.address] = gatt
                connectedDevices[gatt.device.address] = true
                onConnect?.invoke()
            }
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                pairedDevices[gatt.device.address]?.close()
                connectedDevices[gatt.device.address] = false
                onDisconnect?.invoke()
            }
        }
    }

    fun performBluetoothDeviceConnectOperation(scanResult: ScanResult, context: Context) {
        if (checkDeviceConnection(scanResult.device.address) == true) {
            disconnectFromBluetoothDevice(scanResult.device)
        } else {
            connectToBluetoothDevice(scanResult.device, context)
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToBluetoothDevice(bluetoothDevice: BluetoothDevice, context: Context) {
        bluetoothDevice.connectGatt(
            context,
            false,
            mGattCallback,
            BluetoothDevice.TRANSPORT_LE
        )
    }

    @SuppressLint("MissingPermission")
    fun disconnectFromBluetoothDevice(bluetoothDevice: BluetoothDevice) {
        pairedDevices[bluetoothDevice.address].let {
            it?.disconnect()
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnectFromAllBluetoothDevices() {
        connectedDevices.forEach {
            if (it.value) {
                pairedDevices[it.key]?.disconnect()
            }
        }
    }
}
