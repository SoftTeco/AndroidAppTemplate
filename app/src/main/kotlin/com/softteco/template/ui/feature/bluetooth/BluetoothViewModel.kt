package com.softteco.template.ui.feature.bluetooth

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.softteco.template.MainActivity
import com.softteco.template.data.utils.EMPTY_STRING
import com.softteco.template.ui.feature.bluetooth.modules.BluetoothFunctionType
import com.softteco.template.ui.feature.bluetooth.modules.BluetoothHelper
import com.softteco.template.ui.feature.bluetooth.modules.BluetoothLibraryType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

private const val TICKER_PERIOD = 3000L
private const val TICKER_INIT_DELAY = 1500L

class BluetoothViewModel @Inject constructor() : ViewModel() {

    private val _bluetoothDevices = mutableMapOf<String, BluetoothDevice>()
    private lateinit var bluetoothHelper: BluetoothHelper

    fun initBluetooth(activity: MainActivity) {
        bluetoothHelper = BluetoothHelper(activity, this)
        bluetoothHelper.initBluetooth()
    }

    fun setBluetoothLibraryType(bluetoothLibraryType: BluetoothLibraryType) {
        bluetoothHelper.setBluetoothLibraryType(bluetoothLibraryType)
    }

    fun setBluetoothFunctionType(bluetoothFunctionType: BluetoothFunctionType) {
        bluetoothHelper.setBluetoothFunctionType(bluetoothFunctionType)
    }

    fun provideBluetoothOperation() {
        bluetoothHelper.provideBluetoothOperation()
    }

    @SuppressLint("MissingPermission")
    fun addBluetoothDevice(macAddress: String, device: Any) {
        when (device) {
            is com.polidea.rxandroidble3.scan.ScanResult -> {
                _bluetoothDevices[macAddress] =
                    BluetoothDevice(device.bleDevice.name ?: EMPTY_STRING, device.bleDevice.macAddress, device.rssi)
            }

            is no.nordicsemi.android.support.v18.scanner.ScanResult -> {
                _bluetoothDevices[macAddress] =
                    BluetoothDevice(device.device.name, device.device.address, device.rssi)
            }
        }
    }

    val devicesFlow = tickerFlow().transform {
        val devices = _bluetoothDevices.toList().map { btDeviceList -> btDeviceList.second }
            .sortedBy { it.dBmLevel }
        emit(devices)
    }
}

@Immutable
data class BluetoothDevice(
    val name: String = "",
    val macAddress: String = "",
    val dBmLevel: Int = 0
)

private fun tickerFlow(period: Long = TICKER_PERIOD, initialDelay: Long = TICKER_INIT_DELAY) =
    flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }