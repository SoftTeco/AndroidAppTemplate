package com.softteco.template.ui.feature.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import com.softteco.template.Constants.ACTION_NOTIFICATION_REPLY
import com.softteco.template.Constants.NOTIFICATION_REPLY
import timber.log.Timber

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_NOTIFICATION_REPLY) {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            if (remoteInput != null) {
                val inputText = remoteInput.getCharSequence(NOTIFICATION_REPLY).toString()
                Timber.tag("inputted text:").d(inputText)
            }
        }
    }
}