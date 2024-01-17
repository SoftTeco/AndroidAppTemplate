package com.softteco.template.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.google.firebase.messaging.RemoteMessage
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import com.softteco.template.Constants
import com.softteco.template.R
import com.softteco.template.ui.feature.notifications.SmartReplyBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NotificationHelper @Inject constructor(private val context: Context) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var repliesList = mutableListOf<String>()

    fun createNotificationLayout(message: RemoteMessage.Notification): RemoteViews {
        val notificationLayout =
            RemoteViews(context.packageName, R.layout.notification_content_view)
        notificationLayout.setTextViewText(R.id.notification_title, message.title)
        notificationLayout.setTextViewText(R.id.notification_body, message.body)
        return notificationLayout
    }

    fun createNotificationContentLayout(message: RemoteMessage.Notification): RemoteViews {
        val notificationLayout =
            RemoteViews(context.packageName, R.layout.notification_replies_view)

        coroutineScope.launch {
            updateNotificationContentLayout(notificationLayout, message)
        }

        return notificationLayout
    }

    private suspend fun updateNotificationContentLayout(
        notificationLayout: RemoteViews,
        message: RemoteMessage.Notification
    ) {
        notificationLayout.setTextViewText(R.id.choiсe_first_text, repliesList.getOrNull(0) ?: "")
        notificationLayout.setTextViewText(R.id.choiсe_second_text, repliesList.getOrNull(1) ?: "")
        notificationLayout.setTextViewText(R.id.choiсe_third_text, repliesList.getOrNull(2) ?: "")

        notificationLayout.setTextViewText(R.id.notification_title_content, message.title)
        notificationLayout.setTextViewText(R.id.notification_body_content, message.body)

        notificationLayout.setOnClickPendingIntent(
            R.id.choiсe_first_text,
            createPendingIntentForSmartReply(Constants.NOTIFICATION_SMART_REPLY_FIRST, repliesList.getOrNull(0))
        )
        notificationLayout.setOnClickPendingIntent(
            R.id.choiсe_second_text,
            createPendingIntentForSmartReply(Constants.NOTIFICATION_SMART_REPLY_SECOND, repliesList.getOrNull(1))
        )
        notificationLayout.setOnClickPendingIntent(
            R.id.choiсe_third_text,
            createPendingIntentForSmartReply(Constants.NOTIFICATION_SMART_REPLY_THIRD, repliesList.getOrNull(2))
        )
    }

    private fun createPendingIntentForSmartReply(action: String, reply: String?): PendingIntent {
        val intent = Intent(context, SmartReplyBroadcastReceiver::class.java)
        intent.action = action
        intent.putExtra(Constants.REPLY, reply)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    suspend fun getChoiceList(message: RemoteMessage.Notification): MutableList<String> =
        suspendCancellableCoroutine { continuation ->
            val conversation = mutableListOf<TextMessage>()
            val smartReplyGenerator = SmartReply.getClient()
            conversation.add(
                TextMessage.createForLocalUser(
                    message.body.toString(),
                    System.currentTimeMillis()
                )
            )
            smartReplyGenerator.suggestReplies(conversation).addOnSuccessListener { result ->
                if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                    repliesList.clear()
                    for (suggestion in result.suggestions) {
                        val replyText = suggestion.text.replace("\n", "")
                        repliesList.add(replyText)
                    }
                }
                continuation.resume(repliesList)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
}
