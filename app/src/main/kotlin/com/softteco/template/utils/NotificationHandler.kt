package com.softteco.template.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.softteco.template.Constants
import com.softteco.template.MainActivity
import com.softteco.template.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationHandler@Inject constructor(
    private val context: Context,
    private val notificationHelper: NotificationHelper
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var remoteInput: RemoteInput

    fun handleMessage(message: RemoteMessage.Notification) {
        coroutineScope.launch {
            val answersList = notificationHelper.getChoiceList(message)

            val notificationLayout = notificationHelper.createNotificationLayout(message)
            val channelId = context.getString(R.string.default_notification_channel_id)

            val notificationBuilder = if (answersList.isNotEmpty()) {
                val notificationContentLayout =
                    notificationHelper.createNotificationContentLayout(message)
                createNotificationBuilderWithContent(
                    channelId,
                    notificationLayout,
                    notificationContentLayout,
                    createPendingIntent(createNotificationIntent(message)),
                    createReplyAction(createPendingIntent(createReplyIntent()), createRemoteInput())
                )
            } else {
                createNotificationBuilder(
                    channelId,
                    notificationLayout,
                    createPendingIntent(createNotificationIntent(message)),
                    createReplyAction(createPendingIntent(createReplyIntent()), createRemoteInput())
                )
            }

            createNotificationChannel(channelId)
            showNotification(notificationBuilder)
        }
    }

    private fun createNotificationBuilderWithContent(
        channelId: String,
        notificationLayout: RemoteViews,
        notificationContentLayout: RemoteViews,
        pendingIntent: PendingIntent,
        replyAction: NotificationCompat.Action
    ): Notification {
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setAutoCancel(true)
            setStyle(NotificationCompat.DecoratedCustomViewStyle())
            setCustomContentView(notificationLayout)
            setCustomBigContentView(notificationContentLayout)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setContentIntent(pendingIntent)
            addAction(replyAction)
        }.build()
    }

    private fun createNotificationBuilder(
        channelId: String,
        notificationLayout: RemoteViews,
        pendingIntent: PendingIntent,
        replyAction: NotificationCompat.Action
    ): Notification {
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setAutoCancel(true)
            setStyle(NotificationCompat.DecoratedCustomViewStyle())
            setCustomContentView(notificationLayout)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setContentIntent(pendingIntent)
            addAction(replyAction)
        }.build()
    }

    private fun createReplyIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            action = Constants.ACTION_NOTIFICATION_REPLY
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(Notification.EXTRA_NOTIFICATION_ID, 0)
        }
    }

    private fun createNotificationIntent(message: RemoteMessage.Notification): Intent {
        return Intent(context, MainActivity::class.java).apply {
            action = Constants.ACTION_NOTIFICATION
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notificationTitle", message.title)
            putExtra("notificationBody", message.body)
        }
    }

    private fun createPendingIntent(intent: Intent): PendingIntent {
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    private fun createRemoteInput(): RemoteInput {
        remoteInput = RemoteInput.Builder(Constants.NOTIFICATION_REPLY)
            .setLabel(ContextCompat.getString(context, R.string.enter_text))
            .build()
        return remoteInput
    }
    private fun createReplyAction(
        replyPendingIntent: PendingIntent,
        remoteInput: RemoteInput
    ): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            0,
            ContextCompat.getString(context, R.string.reply),
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()
    }

    private fun createNotificationChannel(channelId: String) {
        val channel = NotificationChannel(
            channelId,
            Constants.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
    }

    private fun showNotification(notificationBuilder: Notification) {
        NotificationManagerCompat.from(context).apply {
            manager.notify(Constants.NOTIFICATION_ID, notificationBuilder)
        }
    }
}
