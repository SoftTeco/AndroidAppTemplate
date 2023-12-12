package com.softteco.template.ui.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.bluetooth.BluetoothHelper
import com.softteco.template.ui.components.SnackBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.nordicsemi.android.support.v18.scanner.ScanResult
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothHelper: BluetoothHelper
) : ViewModel() {

    private val _snackBarState = MutableStateFlow(SnackBarState())
    private var _bluetoothDevices = hashMapOf<String, ScanResult>()
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    private val mutex = Mutex()
    private var filtered: Boolean = true
    var filteredName: String = ""

    val state = combine(
        _snackBarState,
        _devices
    ) { snackBar, devices ->
        State(
            devices = devices,
            snackBar = snackBar,
            dismissSnackBar = { _snackBarState.value = SnackBarState() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    @SuppressLint("MissingPermission")
    fun addScanResult(scanResult: ScanResult) {
        viewModelScope.launch(Dispatchers.IO) {
            mutex.withLock {
                addDevice(scanResult)
            }
        }
    }

    fun disconnectFromDevice() {
        bluetoothHelper.disconnectFromDevice()
    }

    fun registerReceiver() {
        bluetoothHelper.registerReceiver()
    }

    fun unregisterReceiver() {
        bluetoothHelper.unregisterReceiver()
    }

    fun provideOperation() {
        bluetoothHelper.provideOperation()
    }

    fun connectToDevice(bluetoothDevice: BluetoothDevice) {
        bluetoothHelper.connectToDevice(bluetoothDevice)
    }

    fun provideOnScanCallback(onScanResult: (scanResult: ScanResult) -> Unit) {
        bluetoothHelper.provideOnScanCallback(onScanResult)
    }

    fun provideOnConnectCallback(onConnect: () -> Unit) {
        bluetoothHelper.provideOnConnectCallback(onConnect)
    }

    fun setFiltered(filtered: Boolean) {
        this.filtered = filtered
        emitDevices()
    }

    @SuppressLint("MissingPermission")
    private fun emitDevices() {
        _devices.value =
            _bluetoothDevices.toList().asReversed().map { it.second.device }.let { devices ->
                if (filtered) devices.filter { it.name == filteredName } else devices
            }
    }

    @SuppressLint("MissingPermission")
    private fun addDevice(scanResult: ScanResult) {
        _bluetoothDevices[scanResult.device.address] = scanResult
        emitDevices()
    }

    @Immutable
    data class State(
        val devices: List<BluetoothDevice> = emptyList(),
        val snackBar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {}
    )
}
