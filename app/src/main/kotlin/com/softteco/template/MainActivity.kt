package com.softteco.template

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.navigation.Graph
import com.softteco.template.ui.AppContent
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    lateinit var sessionManager: SessionManager

    @SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val theme = dataStore.data.map {
                it[PreferencesKeys.THEME_MODE]
            }.collectAsState(initial = ThemeMode.SystemDefault.value)
            val appThemeContent: @Composable () -> Unit = {
                val isUserLoggedIn by sessionManager.isUserLoggedIn.collectAsState(initial = false)
                val startDestination =
                    if (isUserLoggedIn) Graph.BottomBar.route else Graph.Login.route
                AppContent(startDestination)
            }
            theme.value?.let {
                AppTheme(themeMode = it, content = appThemeContent)
            } ?: AppTheme(themeMode = ThemeMode.SystemDefault.value, content = appThemeContent)
        }
    }
}
