package com.softteco.template.ui.feature.notifications

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softteco.template.Constants.ACTION_NOTIFICATION
import com.softteco.template.Constants.ACTION_NOTIFICATION_REPLY
import com.softteco.template.Constants.CHANNEL_NAME
import com.softteco.template.Constants.NOTIFICATION_ID
import com.softteco.template.Constants.NOTIFICATION_REPLY
import com.softteco.template.MainActivity
import com.softteco.template.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            sendNotification(message)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        val channelId = this.getString(R.string.default_notification_channel_id)
        val channel = NotificationChannel(channelId, CHANNEL_NAME, IMPORTANCE_DEFAULT)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val intent = createNotificationIntent()
        val replyIntent = createReplyIntent()

        val pendingIntent = createPendingIntent(intent)
        val replyPendingIntent = createPendingIntent(replyIntent)

        val notificationLayout = createNotificationLayout(message)

        val remoteInput = createRemoteInput()

        val replyAction = createReplyAction(replyPendingIntent, remoteInput)

        val notificationBuilder =
            createNotificationBuilder(channelId, notificationLayout, pendingIntent, replyAction)

        NotificationManagerCompat.from(this).apply {
            manager.notify(NOTIFICATION_ID, notificationBuilder)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("FCM new token:").d(token)
    }

    private fun createNotificationBuilder(
        channelId: String,
        notificationLayout: RemoteViews,
        pendingIntent: PendingIntent,
        replyAction: NotificationCompat.Action
    ): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(replyAction)
            .build()
    }

    private fun createNotificationIntent(): Intent {
        return Intent(this, AppFirebaseMessagingService::class.java).apply {
            action = ACTION_NOTIFICATION
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // tap actions on notification
        }
    }

    private fun createReplyIntent(): Intent {
        return Intent(this, MainActivity::class.java).apply {
            action = ACTION_NOTIFICATION_REPLY
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
    }

    private fun createPendingIntent(intent: Intent): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_MUTABLE
        )
    }

    private fun createNotificationLayout(message: RemoteMessage.Notification): RemoteViews {
        val notificationLayout = RemoteViews(packageName, R.layout.notification_content_view)
        notificationLayout.setTextViewText(R.id.notification_title, message.title)
        notificationLayout.setTextViewText(R.id.notification_body, message.body)
        return notificationLayout
    }

    private fun createRemoteInput(): RemoteInput {
        return RemoteInput.Builder(NOTIFICATION_REPLY).run {
            setLabel(getString(R.string.enter_text))
            build()
        }
    }

    private fun createReplyAction(
        replyPendingIntent: PendingIntent,
        remoteInput: RemoteInput
    ): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            0,
            getString(R.string.reply),
            replyPendingIntent
        ).addRemoteInput(remoteInput)
            .build()
    }
}
