package com.softteco.template.data.base.bluetooth

data class BluetoothDevice(
    var scanResultRxAndroidBle: com.polidea.rxandroidble3.scan.ScanResult,
    var scanResultNordicSemi: no.nordicsemi.android.support.v18.scanner.ScanResult
)