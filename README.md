<img src="app/src/main/ic_launcher-playstore.png" alt="icon" width="100"/>

# AndroidBLE

[![License: MIT](https://img.shields.io/badge/license-MIT-yellow)](https://opensource.org/licenses/MIT)
![compileSdk](https://img.shields.io/badge/dynamic/toml?url=https://raw.githubusercontent.com/SoftTeco/AndroidBLE/main/gradle/libs.versions.toml&query=$.versions.compileSdk&label=compileSdk&color=green)
![minSdk](https://img.shields.io/badge/dynamic/toml?url=https://raw.githubusercontent.com/SoftTeco/AndroidBLE/main/gradle/libs.versions.toml&query=$.versions.minSdk&label=minSdk&color=green)
![targetSdk](https://img.shields.io/badge/dynamic/toml?url=https://raw.githubusercontent.com/SoftTeco/AndroidBLE/main/gradle/libs.versions.toml&query=$.versions.targetSdk&label=targetSdk&color=green)
[![codecov](https://codecov.io/gh/SoftTeco/AndroidBLE/graph/badge.svg)](https://codecov.io/gh/SoftTeco/AndroidBLE)
![lint workflow](https://github.com/SoftTeco/AndroidBLE/actions/workflows/lint.yml/badge.svg)

Based on [AndroidAppTemplate](https://github.com/SoftTeco/AndroidAppTemplate)

## Overview

AndroidBLE is an android application that provides the ability to receive data on temperature and humidity from a peripheral sensor via Bluetooth. It is built using the Kotlin programming language and leverages the following technologies:

- [BLE](https://developer.android.com/develop/connectivity/bluetooth/ble/ble-overview): Bluetooth Low Energy.
- [NordicSemiconductor](https://github.com/NordicSemiconductor/Android-Scanner-Compat-Library): Library for scanning available Bluetooth devices.
- [Room](https://developer.android.com/training/data-storage/room): Library for saving data in a database.
- [Vico](https://patrykandpatrick.com/vico/wiki/getting-started/): Library for displaying data on graphs.

## Available Features

- **Bluetooth**:
  - **Scanning**: Scan and display temperature and humidity devices, with automatic filtering by device name.
  - **Connection**: Connect to two or more devices simultaneously.
  - **Receiving data**: Receive temperature and humidity data from connected devices in the background.
- **Saving data**: Saving the obtained data on temperature and humidity in the database.
- **Graphical view**: View saved temperature and humidity data on a graph.
- **Error Handling and Logging**: Comprehensive error handling and logging throughout the application.

## Configuration

The application can be configured using environment variables. The following environment variables are available:

- `BASE_URL`: The base URL of the application.
- `BLUETOOTH_SERVICE_UUID_VALUE`: UUID of service with data from the device.
- `BLUETOOTH_CHARACTERISTIC_UUID_VALUE`: UUID of characteristic with data from the device.
- `BLUETOOTH_DESCRIPTOR_UUID_VALUE`: UUID of descriptor required to receive data from the device.

## Supported devices

The application currently supports working with Lywsd03mmc sensor. In the future, it is planned to add support for working with the same sensor via the Zigbee protocol.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

RustBackendTemplate is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.
