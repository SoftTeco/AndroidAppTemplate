package com.softteco.template.ui.feature.chart

import android.annotation.SuppressLint
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.bluetooth.entity.DataLYWSD03MMC
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.data.bluetooth.BluetoothHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val bluetoothHelper: BluetoothHelper
) : ViewModel() {

    private var snackBarState = MutableStateFlow(SnackBarState())
    private var dataFromLYWSD03MMC = MutableStateFlow(DataLYWSD03MMC())

    val state = combine(
        snackBarState,
        dataFromLYWSD03MMC
    ) { snackBar, dataLYWSD03MMC ->
        State(
            dataLYWSD03MMC = dataLYWSD03MMC,
            snackBar = snackBar,
            dismissSnackBar = { snackBarState.value = SnackBarState() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    fun provideOnDeviceResultCallback(onDeviceResult: (dataLYWSD03MMC: DataLYWSD03MMC) -> Unit) {
        bluetoothHelper.provideOnDeviceResultCallback(onDeviceResult)
    }

    @SuppressLint("MissingPermission")
    fun provideDataLYWSD03MMC(dataLYWSD03MMC: DataLYWSD03MMC) {
        viewModelScope.launch {
            dataFromLYWSD03MMC.value = dataLYWSD03MMC
        }
    }

    @Immutable
    data class State(
        val dataLYWSD03MMC: DataLYWSD03MMC = DataLYWSD03MMC(),
        val snackBar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {}
    )
}
