package com.softteco.template.ui.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class BluetoothViewModel @Inject constructor() : ViewModel() {

    private val _snackBarState = MutableStateFlow(SnackBarState())
    private var _bluetoothDevices = hashMapOf<String, ScanResult>()
    private val _devices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    private val mutex = Mutex()

    val state = combine(
        _snackBarState,
        _devices,
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
    fun addScanResult(scanResult: ScanResult, deviceName: String) {
        if(scanResult.device.name.equals(deviceName)) {
            viewModelScope.launch(Dispatchers.IO) {
                mutex.withLock {
                    addDevice(scanResult)
                }
            }
        }
    }

    private fun addDevice(scanResult: ScanResult) {
        _bluetoothDevices[scanResult.device.address] = scanResult
        val list = _bluetoothDevices.toList().asReversed()
        _devices.value = list.map { it.second.device }
    }

    @Immutable
    data class State(
        val devices: List<BluetoothDevice> = emptyList(),
        val snackBar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {}
    )
}
