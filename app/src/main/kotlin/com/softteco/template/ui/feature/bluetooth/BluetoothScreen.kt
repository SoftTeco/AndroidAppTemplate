package com.softteco.template.ui.feature.bluetooth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.MainActivity
import com.softteco.template.ui.feature.bluetooth.modules.BluetoothFunctionType
import com.softteco.template.ui.feature.bluetooth.modules.BluetoothLibraryType
import com.softteco.template.ui.theme.Dimens

@Composable
fun BluetoothScreen(
    viewModel: BluetoothViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
) {
    val activity = LocalContext.current as MainActivity
    viewModel.initBluetooth(activity)
    viewModel.provideBluetoothOperation()

    ScreenContent(
        viewModel,
    )
}

@Composable
private fun ScreenContent(
    viewModel: BluetoothViewModel,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)) {
        CustomTabs(viewModel)

    }
}

@Composable
fun CustomTabs(viewModel: BluetoothViewModel) {
    var selectedFunctionIndex by remember { mutableIntStateOf(0) }
    var selectedLibraryIndex by remember { mutableIntStateOf(0) }

    val listWithFunctions = listOf("Send", "Receive")
    val listWithLibraries = listOf("RxAndroidBle", "NordicSemi")

    val modifier = Modifier
        .padding(vertical = 4.dp, horizontal = 8.dp)
        .clip(RoundedCornerShape(50))
        .padding(1.dp)

    val modifierSelected = Modifier
        .clip(RoundedCornerShape(50))
        .background(
            MaterialTheme.colorScheme.primary
        )

    val modifierUnselected = Modifier
        .clip(RoundedCornerShape(50))
        .background(
            MaterialTheme.colorScheme.onBackground
        )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)) {
        TabRow(selectedTabIndex = selectedFunctionIndex,
            containerColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier,
            indicator = {
                Box {}
            }
        ) {
            listWithFunctions.forEachIndexed { index, text ->
                val selected = selectedFunctionIndex == index
                Tab(
                    modifier = if (selected) modifierSelected
                    else modifierUnselected,
                    selected = selected,
                    onClick = {
                        selectedFunctionIndex = index
                        when (index) {
                            0 -> viewModel.setBluetoothFunctionType(BluetoothFunctionType.SEND)
                            1 -> viewModel.setBluetoothFunctionType(BluetoothFunctionType.RECEIVE)
                        }
                        viewModel.provideBluetoothOperation()
                    },
                    text = { Text(text = text, color = MaterialTheme.colorScheme.onSecondary) }
                )
            }
        }

        TabRow(selectedTabIndex = selectedLibraryIndex,
            containerColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier,
            indicator = {
                Box {}
            }
        ) {
            listWithLibraries.forEachIndexed { index, text ->
                val selected = selectedLibraryIndex == index
                Tab(
                    modifier = if (selected) modifierSelected
                    else modifierUnselected,
                    selected = selected,
                    onClick = {
                        selectedLibraryIndex = index
                        when (index) {
                            0 -> viewModel.setBluetoothLibraryType(BluetoothLibraryType.RX_ANDROID_BLE)
                            1 -> viewModel.setBluetoothLibraryType(BluetoothLibraryType.NORDIC_BLE)
                        }
//                        viewModel.provideBluetoothOperation()
                    },
                    text = { Text(text = text, color = MaterialTheme.colorScheme.onSecondary) }
                )
            }
        }
    }
}

//@Composable
//fun BluetoothDeviceList(
//    bluetoothDevices: List<BluetoothViewModel.BluetoothDevice?>,
//    onBluetoothDeviceConnectClicked: () -> Unit = {}
//) {
//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        item {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//                    .padding(vertical = 25.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {}
//        }
//        items(bluetoothDevices) { bluetoothDevice ->
//            bluetoothDevice?.macAddress?.let {
//                bluetoothDevice?.name?.let { it1 ->
//                    bluetoothDevice?.dBmLevel?.let { it2 ->
//                        BluetoothDeviceCard(
//                            it1,
//                            it,
//                            it2,
//                            onBluetoothDeviceConnectClicked
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun BluetoothDeviceCard(
//    name: String,
//    macAddress: String,
//    dBmLevel: String,
//    onBluetoothDeviceConnectClicked: () -> Unit = {}
//) {
//    Card(
//        modifier = Modifier
//            .padding(10.dp)
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        shape = MaterialTheme.shapes.medium
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Image(
//                painter = painterResource(R.drawable.ic_bluetooth),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(130.dp)
//                    .padding(8.dp),
//                contentScale = ContentScale.Fit
//            )
//            Column(Modifier.padding(8.dp)) {
//                Text(
//                    text = name,
//                    style = MaterialTheme.typography.headlineMedium
//                )
//                Text(
//                    text = macAddress,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//                Text(
//                    text = dBmLevel,
//                    style = MaterialTheme.typography.titleMedium
//                )
//                Button(onClick = onBluetoothDeviceConnectClicked) {
//                    Text(R.string.bluetooth_device_connect.toString())
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun TabRow(
//    selectedTabIndex: Int,
//    modifier: Modifier = Modifier,
//    indicator: @Composable @UiComposable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
//        TabRowDefaults.Indicator(
//            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
//        )
//    },
//    divider: @Composable @UiComposable () -> Unit = @Composable {
////        TabRowDefaults.Divider()
//    },
//    tabs: @Composable @UiComposable () -> Unit
//): Unit {
//}
//
//@Composable
//fun BluetoothDeviceListItem(bluetoothDevice: BluetoothViewModel.BluetoothDevice) {
//    Text(text = bluetoothDevice.name)
//}