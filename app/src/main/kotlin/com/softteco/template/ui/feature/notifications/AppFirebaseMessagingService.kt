package com.softteco.template.ui.feature.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softteco.template.utils.NotificationHandler
import com.softteco.template.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationHandler by lazy {
        NotificationHandler(
            this,
            NotificationHelper(this)
        )
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            notificationHandler.handleMessage(message)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM new token:").d(token)
    }
}
