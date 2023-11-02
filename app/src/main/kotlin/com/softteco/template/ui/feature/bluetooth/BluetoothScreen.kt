package com.softteco.template.ui.feature.bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingNormal

@Composable
fun BluetoothScreen(
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    ScreenContent(
        state,
        modifier,
        onBackClicked
    )
}

@Composable
private fun ScreenContent(
    state: BluetoothViewModel.State,
    modifier: Modifier,
    onBackClicked: () -> Unit
) {
    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackBar.textId),
        showSnackbar = state.snackBar.show,
        onDismissSnackbar = state.dismissSnackBar
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CustomTopAppBar(
                stringResource(id = R.string.bluetooth),
                showBackIcon = true,
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = onBackClicked
            )
            BluetoothDevicesList(state)
        }
    }
}

@Composable
private fun BluetoothDevicesList(
    state: BluetoothViewModel.State
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(PaddingDefault),
        verticalArrangement = Arrangement.spacedBy(PaddingNormal)
    ) {
        items(state.bluetoothDevices) { bluetoothDevice ->
            BluetoothDeviceCard(
                bluetoothDevice.name,
                bluetoothDevice.macAddress,
                bluetoothDevice.rssi
            )
        }
    }
}

@Composable
private fun BluetoothDeviceCard(
    name: String,
    macAddress: String,
    rssi: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(all = PaddingDefault)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.baseline_bluetooth_24),
                contentDescription = stringResource(id = R.string.bluetooth_icon_description)
            )
            Spacer(Modifier.weight(1f))
            Column {
                Text(text = name)
                Text(text = macAddress)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_signal_level_24),
                        contentDescription = stringResource(id = R.string.signal_level_icon_description)
                    )
                    Text(text = rssi.toString())
                }
            }
            Spacer(Modifier.weight(2f))
            Button(onClick = {}) {
                Text(stringResource(id = R.string.connect))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            BluetoothViewModel.State(),
            Modifier
        ) {}
    }
}
