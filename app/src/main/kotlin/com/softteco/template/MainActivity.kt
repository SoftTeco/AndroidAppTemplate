package com.softteco.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.softteco.template.ui.AppContent
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.utils.BluetoothHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        BluetoothHelper.initBluetoothHelper(this)
        setContent {
            AppTheme {
                AppContent()
            }
        }
    }
}
