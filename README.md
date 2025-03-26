<img src="app/src/main/ic_launcher-playstore.png" alt="icon" width="100"/>

# Template
![test workflow](https://github.com/SoftTeco/AndroidAppTemplate/actions/workflows/test.yml/badge.svg)
[![codecov](https://codecov.io/gh/SoftTeco/AndroidAppTemplate/graph/badge.svg)](https://codecov.io/gh/SoftTeco/AndroidAppTemplate)
![lint workflow](https://github.com/SoftTeco/AndroidAppTemplate/actions/workflows/lint.yml/badge.svg)

Sample application to demonstrate usage of Jetpack Compose, Kotlin Flow, Android Hilt, etc.

## Overview

* Jetpack Compose
* Compose Navigation
* Showing Snackbars with Compose
* Coroutines & Kotlin Flow
* Android Hilt - for Dependency Injection
* Unit testing - with Mockito & JUnit
* Retrofit - for Network Requests
* Coil - for Image loading
* Dark & Light Modes
* Detekt - for checking code style

To enable Detekt, execute `pre-commit install` in the terminal from the project root folder.

###  To set the admin of a fully managed device, follow these steps:
1. Enable developer mode on your device;
2. Enable USB debugging;
3. Install the app;
4. Run the following command in the Android Debug Bridge (adb) shell:
```bash
    adb shell dpm set-device-owner com.softteco.template/.AppDeviceAdminReceiver
```
5. To remove the admin of a fully managed device:
```bash
    adb shell dpm remove-active-admin com.softteco.template/.AppDeviceAdminReceiver
```
