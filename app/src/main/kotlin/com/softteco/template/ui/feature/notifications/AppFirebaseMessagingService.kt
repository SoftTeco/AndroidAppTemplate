package com.softteco.template.ui.feature.notifications

import android.annotation.SuppressLint
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softteco.template.Constants.ACTION_NOTIFICATION_REPLY
import com.softteco.template.Constants.NOTIFICATION_REPLY
import com.softteco.template.R
import kotlin.random.Random
import timber.log.Timber

class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val random = Random

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            sendNotification(message)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, AppFirebaseMessagingService::class.java).apply {
            action = ACTION_NOTIFICATION_REPLY
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val replyIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = ACTION_NOTIFICATION_REPLY
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_MUTABLE
        )
        val replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, replyIntent, FLAG_MUTABLE)

        val channelId = this.getString(R.string.default_notification_channel_id)

        val notificationLayout = RemoteViews(packageName, R.layout.notification_content_view)
        notificationLayout.setTextViewText(R.id.notification_title, message.title)
        notificationLayout.setTextViewText(R.id.notification_body, message.body)

        val remoteInput = RemoteInput.Builder(NOTIFICATION_REPLY)
            .setLabel(getString(R.string.enter_text))
            .build()

        val action = NotificationCompat.Action.Builder(
            null,
            getString(R.string.reply), replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(action)

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
