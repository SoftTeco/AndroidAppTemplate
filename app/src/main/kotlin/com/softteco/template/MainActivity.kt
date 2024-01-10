package com.softteco.template

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.softteco.template.Constants.CHANNEL_NAME
import com.softteco.template.Constants.NOTIFICATION_ID
import com.softteco.template.navigation.Graph
import com.softteco.template.ui.AppContent
import com.softteco.template.ui.feature.notifications.NotificationViewModel
import com.softteco.template.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleNotification()

        setContent {
            val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()
            val theme by viewModel.theme.collectAsState()

            // Keep the splash screen on-screen while we check if the user is logged in
            val content: View = findViewById(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        return if (isUserLoggedIn != null) {
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            false
                        }
                    }
                }
            )

            key(isUserLoggedIn) {
                AppTheme(theme) {
                    when (isUserLoggedIn) {
                        true -> AppContent(Graph.BottomBar.route)
                        false -> AppContent(Graph.Login.route)
                        null -> { /*NOOP*/
                        }
                    }
                }
            }
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
        }
    }
}
