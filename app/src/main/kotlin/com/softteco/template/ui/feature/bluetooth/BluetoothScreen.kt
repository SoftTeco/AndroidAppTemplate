package com.softteco.template.ui.feature.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.OnLifecycleEvent
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.utils.BluetoothHelper
import kotlinx.coroutines.launch

@Composable
fun BluetoothScreen(
    onConnect: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val deviceName = stringResource(R.string.device_name)
    BluetoothHelper.onScanResult = {
        viewModel.addScanResult(it, deviceName)
    }
    BluetoothHelper.onConnect = {
        scope.launch {
            onConnect.invoke()
        }
    }
    val state by viewModel.state.collectAsState()
    ScreenContent(
        modifier = modifier,
        state = state,
        onItemClicked = { bluetoothDevice ->
            BluetoothHelper.connectToBluetoothDevice(bluetoothDevice)
        }
    )
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                BluetoothHelper.disconnectFromBluetoothDevice()
                BluetoothHelper.registerReceiver()
                BluetoothHelper.provideBluetoothOperation()
            }

            Lifecycle.Event.ON_PAUSE -> {
                BluetoothHelper.unregisterReceiver()
            }

            else -> {}
        }
    }
}

@Composable
private fun ScreenContent(
    state: BluetoothViewModel.State,
    onItemClicked: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        CustomTopAppBar(
            stringResource(id = R.string.bluetooth),
            modifier = Modifier.fillMaxWidth()
        )
        BluetoothDevicesList(
            devices = state.devices,
            onItemClicked = onItemClicked,
        )
    }
}

@Composable
fun BluetoothDevicesList(
    devices: List<BluetoothDevice>,
    onItemClicked: (BluetoothDevice) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        devices.forEach { device ->
            item {
                BluetoothDeviceCard(
                    bluetoothDevice = device,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceCard(
    bluetoothDevice: BluetoothDevice,
    onItemClicked: (BluetoothDevice) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(vertical = Dimens.PaddingSmall, horizontal = Dimens.PaddingDefault)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .clickable { onItemClicked(bluetoothDevice) }
                .padding(Dimens.PaddingSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_bluetooth_24),
                contentDescription = "ic_bluetooth",
                modifier = Modifier.padding(Dimens.PaddingSmall),
                contentScale = ContentScale.Fit
            )
            Column(Modifier.weight(1F)) {
                Text(
                    text = bluetoothDevice.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = bluetoothDevice.address,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            // TODO: to show it after item clicked, before chart screen opened
            /*            CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )*/
        }
    }
}
