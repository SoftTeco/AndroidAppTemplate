package com.softteco.template.data.bluetooth.entity

import no.nordicsemi.android.support.v18.scanner.ScanResult

data class BluetoothState(
    var onConnect: (() -> Unit)? = null,
    var onDisconnect: (() -> Unit)? = null,
    var onScanResult: ((scanResult: ScanResult) -> Unit)? = null,
    var onDeviceResult: ((dataLYWSD03MMC: DataLYWSD03MMC) -> Unit)? = null
)