package com.softteco.template.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
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
import com.softteco.template.BuildConfig
import com.softteco.template.Constants.DIVISION_VALUE_OF_VALUES
import com.softteco.template.Constants.END_INDEX_OF_BATTERY
import com.softteco.template.Constants.END_INDEX_OF_TEMPERATURE
import com.softteco.template.Constants.INDEX_OF_HUMIDITY
import com.softteco.template.Constants.START_INDEX_OF_BATTERY
import com.softteco.template.Constants.START_INDEX_OF_TEMPERATURE
import com.softteco.template.MainActivity
import com.softteco.template.data.bluetooth.entity.BluetoothState
import com.softteco.template.data.bluetooth.entity.DataLYWSD03MMC
import com.softteco.template.data.bluetooth.BluetoothHelper
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BluetoothHelperImpl @Inject constructor(): BluetoothHelper {

    private lateinit var activity: MainActivity

    private lateinit var bluetoothReceiver: BroadcastReceiver
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var locationManager: LocationManager
    private var localGatt: BluetoothGatt? = null
    private lateinit var resultBluetoothEnableLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultLocationEnableLauncher: ActivityResultLauncher<Intent>
    var bluetoothState = BluetoothState()

    private val scanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(
            callbackType: Int,
            scanResult: ScanResult
        ) {
            super.onScanResult(callbackType, scanResult)
            scanResult.device.name?.let {
                bluetoothState.onScanResult?.invoke(scanResult)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun init(activity: MainActivity) {
        this.activity = activity
        resultBluetoothEnableLauncher =
            this.activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
        resultLocationEnableLauncher =
            this.activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
        bluetoothReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (
                    intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.STATE_OFF
                    )
                ) {
                    BluetoothAdapter.STATE_ON -> {
                        provideOperation()
                    }

                    BluetoothAdapter.STATE_OFF -> {
                        stopScan()
                    }
                }
            }
        }
        bluetoothManager =
            this.activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        locationManager =
            this.activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun registerReceiver() {
        activity.registerReceiver(
            bluetoothReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
    }

    @Suppress("TooGenericExceptionCaught")
    override fun unregisterReceiver() {
        try {
            activity.unregisterReceiver(bluetoothReceiver)
        } catch (e: Exception) {
            Timber.e("Error unregister receiver", e)
        }
    }

    private fun startScan() {
        stopScan()
        BluetoothLeScannerCompat.getScanner().startScan(scanCallback)
    }

    private fun stopScan() {
        BluetoothLeScannerCompat.getScanner().stopScan(scanCallback)
    }

    private fun makeBluetoothOperation() {
        if (BluetoothPermissionCheckerImpl.hasPermissions(activity)) {
            startScan()
        }
    }

    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                localGatt = gatt
                gatt.discoverServices()
                bluetoothState.onConnect?.invoke()
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                gatt?.let { gatt ->
                    gatt.getService(UUID.fromString(BuildConfig.BLUETOOTH_SERVICE_UUID_VALUE))
                        .getCharacteristic(UUID.fromString(BuildConfig.BLUETOOTH_CHARACTERISTIC_UUID_VALUE))
                        .let { characteristic ->
                            setCharacteristicNotification(gatt, characteristic, true)
                        }
                }
            }
        }

        @SuppressLint("MissingPermission")
        fun setCharacteristicNotification(
            bluetoothGatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            enable: Boolean
        ): Boolean {
            bluetoothGatt.setCharacteristicNotification(characteristic, enable)
            val descriptor =
                characteristic.getDescriptor(UUID.fromString(BuildConfig.BLUETOOTH_DESCRIPTOR_UUID_VALUE))
            descriptor.value =
                if (enable) {
                    BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                } else {
                    byteArrayOf(
                        0x00,
                        0x00
                    )
                }
            return bluetoothGatt.writeDescriptor(descriptor)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            characteristic.value.let {
                val temp = characteristicByteConversation(
                    characteristic.value,
                    START_INDEX_OF_TEMPERATURE,
                    END_INDEX_OF_TEMPERATURE
                ) / DIVISION_VALUE_OF_VALUES
                val hum = characteristic.value[INDEX_OF_HUMIDITY].toInt()
                val bat = characteristicByteConversation(
                    characteristic.value,
                    START_INDEX_OF_BATTERY,
                    END_INDEX_OF_BATTERY
                )

                bluetoothState.onDeviceResult?.invoke(DataLYWSD03MMC(temp, hum, bat))
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun connectToDevice(bluetoothDevice: BluetoothDevice) {
        bluetoothDevice.connectGatt(
            activity.applicationContext,
            false,
            mGattCallback,
            BluetoothDevice.TRANSPORT_LE
        )
    }

    @SuppressLint("MissingPermission")
    override fun disconnectFromDevice() {
        localGatt?.disconnect()
        localGatt?.close()
    }

    override fun provideOperation() {
        if (BluetoothPermissionCheckerImpl.checkBluetoothSupport(bluetoothAdapter, activity) &&
            BluetoothPermissionCheckerImpl.hasPermissions(activity)
        ) {
            when (
                BluetoothPermissionCheckerImpl.checkEnableDeviceModules(
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
                    makeBluetoothOperation()
                }
            }
        }
    }

    override fun provideOnScanCallback(onScanResult: (scanResult: ScanResult) -> Unit) {
        bluetoothState.onScanResult = onScanResult
    }

    override fun provideOnConnectCallback(onConnect: () -> Unit) {
        bluetoothState.onConnect = onConnect
    }

    override fun provideOnDisconnectCallback(onDisconnect: () -> Unit) {
        bluetoothState.onDisconnect = onDisconnect
    }

    override fun provideOnDeviceResultCallback(onDeviceResult: (dataLYWSD03MMC: DataLYWSD03MMC) -> Unit) {
        bluetoothState.onDeviceResult = onDeviceResult
    }
}
