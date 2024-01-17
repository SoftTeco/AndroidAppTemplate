package com.softteco.template

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.softteco.template.Constants.ACTION_NOTIFICATION
import com.softteco.template.Constants.CHANNEL_NAME
import com.softteco.template.Constants.NOTIFICATION_ID
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.navigation.Graph
import com.softteco.template.ui.AppContent
import com.softteco.template.ui.feature.notifications.NotificationViewModel
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.ThemeMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val notificationViewModel: NotificationViewModel by viewModels()

    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleNotification()
        setContent {
            val theme = dataStore.data.map {
                it[PreferencesKeys.THEME_MODE]
            }.collectAsState(initial = ThemeMode.SystemDefault.value)
            val appThemeContent: @Composable () -> Unit = {
                var isUserLoggedIn by rememberSaveable { mutableStateOf<Boolean?>(null) }
                LaunchedEffect(Unit) {
                    isUserLoggedIn = profileRepository.getUser() is Result.Success
                }
                isUserLoggedIn?.let {
                    val startDestination = if (it) Graph.BottomBar.route else Graph.Login.route
                    AppContent(startDestination)
                }
            }
            theme.value?.let {
                AppTheme(themeMode = it, content = appThemeContent)
            } ?: AppTheme(themeMode = ThemeMode.SystemDefault.value, content = appThemeContent)
        }
    }
    private fun handleNotification() {
        if (intent?.action == Constants.ACTION_NOTIFICATION_REPLY) {
            lifecycleScope.launch {
                notificationViewModel.processRemoteInput(intent)
                notificationViewModel.enteredText.collect { enteredText ->
                    Timber.tag("inputted text:").d(enteredText)
                }
            }

            val channelId = this.getString(R.string.default_notification_channel_id)
            val channel = NotificationChannel(
                channelId,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            manager.cancel(NOTIFICATION_ID)
        } else {
            if (intent?.action == ACTION_NOTIFICATION) {
                val body = intent.getStringExtra("notificationBody")
                lifecycleScope.launch {
                    Timber.tag("inputted body text:").d(body)
                }
            }
        }
    }
}
