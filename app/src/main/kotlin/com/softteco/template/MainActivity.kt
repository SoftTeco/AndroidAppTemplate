package com.softteco.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.navigation.Graph
import com.softteco.template.ui.AppContent
import com.softteco.template.data.bluetooth.BluetoothHelper
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.getFromDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var bluetoothHelper: BluetoothHelper

    @Inject
    @Named("themeMode")
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        bluetoothHelper.init(this)
        setContent {
            val theme = dataStore.getFromDataStore(
                PreferencesKeys.THEME_MODE,
                ThemeMode.SystemDefault.value
            ).collectAsState(initial = ThemeMode.SystemDefault.value)
            AppTheme(themeMode = theme.value) {
                var isUserLoggedIn by rememberSaveable { (mutableStateOf<Boolean?>(null)) }
                LaunchedEffect(Unit) {
                    isUserLoggedIn = profileRepository.getUser() is Result.Success
                }
                isUserLoggedIn?.let {
                    val startDestination = if (it) Graph.BottomBar.route else Graph.Login.route
                    AppContent(startDestination)
                }
            }
        }
    }
}
