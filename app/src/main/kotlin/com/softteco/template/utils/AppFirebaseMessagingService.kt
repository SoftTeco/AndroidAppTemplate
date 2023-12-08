package com.softteco.template.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softteco.template.R
import timber.log.Timber
import kotlin.random.Random

class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val random = Random
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            sendNotification(message)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, AppFirebaseMessagingService::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_IMMUTABLE
        )

        val channelId = this.getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, CHANNEL_NAME, IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)

        manager.notify(random.nextInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM new token:").d(token)
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}
