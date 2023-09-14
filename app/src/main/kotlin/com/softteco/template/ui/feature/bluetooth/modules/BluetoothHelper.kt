package com.softteco.template.ui.feature.bluetooth.modules

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import com.polidea.rxandroidble3.RxBleClient
import com.softteco.template.MainActivity
import com.softteco.template.ui.feature.bluetooth.BluetoothViewModel
import com.softteco.template.ui.feature.bluetooth.modules.libs.getBtDeviceName
import io.reactivex.rxjava3.disposables.Disposable
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback

class BluetoothHelper(
    private val activity: MainActivity,
    private val bluetoothViewModel: BluetoothViewModel,
) {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var locationManager: LocationManager
    private lateinit var bluetoothPermissionChecker: BluetoothPermissionChecker
    @Volatile
    private var bluetoothLibraryType = BluetoothLibraryType.RX_ANDROID_BLE
    @Volatile
    private var bluetoothFunctionType = BluetoothFunctionType.SEND
    private lateinit var rxBleClient: RxBleClient
    private lateinit var bluetoothServer: BluetoothServer

    private var scanBluetoothDevicesDisposable: Disposable? = null
    private var connectBluetoothDevicesDisposable: Disposable? = null

    private var onDisconnect: (() -> Unit) = {

    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(
            callbackType: Int,
            scanResult: no.nordicsemi.android.support.v18.scanner.ScanResult
        ) {
            super.onScanResult(callbackType, scanResult)
            bluetoothViewModel.addBluetoothDevice(
                scanResult.device.address,
                scanResult
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun initBluetooth() {
        bluetoothManager =
            activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        bluetoothPermissionChecker =
            BluetoothPermissionChecker(activity, bluetoothAdapter, locationManager)
        if (bluetoothPermissionChecker.checkBluetoothSupport()) {
            if (bluetoothPermissionChecker.hasPermissions()) {
                bluetoothAdapter.name = getBtDeviceName()
            }
        }
        bluetoothServer = BluetoothServer(bluetoothManager, activity) {}
        rxBleClient = RxBleClient.create(activity)
    }

    fun setBluetoothLibraryType(bluetoothLibraryType: BluetoothLibraryType) {
        this.bluetoothLibraryType = bluetoothLibraryType
    }

    fun setBluetoothFunctionType(bluetoothFunctionType: BluetoothFunctionType) {
        this.bluetoothFunctionType = bluetoothFunctionType
    }

    private fun startScan() {
        when (bluetoothLibraryType) {
            BluetoothLibraryType.NORDIC_BLE -> {
                BluetoothLeScannerCompat.getScanner().startScan(scanCallback)
            }

            BluetoothLibraryType.RX_ANDROID_BLE -> {
                scanBluetoothDevicesDisposable =
                    rxBleClient.scanBleDevices(
                        com.polidea.rxandroidble3.scan.ScanSettings.Builder()
                            .setCallbackType(com.polidea.rxandroidble3.scan.ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .setScanMode(com.polidea.rxandroidble3.scan.ScanSettings.SCAN_MODE_BALANCED)
                            .build()
                    )
                        ?.subscribe(
                            { scanResult ->
                                bluetoothViewModel.addBluetoothDevice(
                                    scanResult.bleDevice.macAddress,
                                    scanResult
                                )
                            }
                        ) {}
            }
        }
    }

    private fun stopScan() {
        when (bluetoothLibraryType) {
            BluetoothLibraryType.NORDIC_BLE -> {
                BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
            }

            BluetoothLibraryType.RX_ANDROID_BLE -> {
                scanBluetoothDevicesDisposable?.dispose()
            }
        }
    }

    fun provideBluetoothOperation() {
        if (bluetoothPermissionChecker.checkBluetoothSupport()) {
            when (bluetoothPermissionChecker.checkEnableDeviceModules()) {
                PermissionType.LOCATION_TURNED_OFF -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    activity.resultLocationEnableLauncher.launch(intent)
                }

                PermissionType.BLUETOOTH_TURNED_OFF -> {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    activity.resultBluetoothEnableLauncher.launch(intent)
                }

                PermissionType.BLUETOOTH_AND_LOCATION_TURNED_ON -> {
                    makeBluetoothOperation()
                }
            }
        }
    }

    private fun makeBluetoothOperation() {
        when (bluetoothFunctionType) {
            BluetoothFunctionType.SEND -> {
                stopReceiving()
                startSending()
            }

            BluetoothFunctionType.RECEIVE -> {
                stopSending()
                startReceiving()
            }
        }
    }

    private fun startSending() {
        startScan()
    }

    private fun stopSending() {
        stopScan()
    }

    private fun startReceiving() {
        bluetoothServer.startAdvertising()
        bluetoothServer.startServer()
    }

    private fun stopReceiving() {
        bluetoothServer.stopAdvertising()
        bluetoothServer.stopServer()
    }
}

enum class BluetoothLibraryType {
    NORDIC_BLE, RX_ANDROID_BLE
}

enum class BluetoothFunctionType {
    SEND, RECEIVE
}