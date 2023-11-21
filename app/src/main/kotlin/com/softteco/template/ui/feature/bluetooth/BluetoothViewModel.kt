package com.softteco.template.ui.feature.bluetooth

import android.annotation.SuppressLint
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

    private var snackBarState = MutableStateFlow(SnackBarState())
    private var bluetoothDevices = linkedMapOf<String, ScanResult>()
    private val devices = MutableStateFlow<List<ScanResult>>(emptyList())
    private val mutex = Mutex()

    val state = combine(
        devices,
        snackBarState
    ) { devices, snackBar ->
        State(
            devices = devices,
            snackBar = snackBar,
            dismissSnackBar = { snackBarState.value = SnackBarState() }
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

    private fun addDevice(scanResult: ScanResult) {
        bluetoothDevices[scanResult.device.address] = scanResult
        val list = bluetoothDevices.toList()
        devices.value = list.map { it.second }
    }

    @Immutable
    data class State(
        val devices: List<ScanResult> = emptyList(),
        val snackBar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {}
    )
}
