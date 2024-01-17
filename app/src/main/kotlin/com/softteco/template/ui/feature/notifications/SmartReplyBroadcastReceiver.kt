package com.softteco.template.ui.feature.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.softteco.template.Constants
import timber.log.Timber

class SmartReplyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == Constants.NOTIFICATION_SMART_REPLY_FIRST ||
            p1?.action == Constants.NOTIFICATION_SMART_REPLY_SECOND ||
            p1?.action == Constants.NOTIFICATION_SMART_REPLY_THIRD
        ) {
            val chosenReply = p1.getStringExtra(Constants.REPLY)
            Timber.tag("value:").d(chosenReply)
        }
    }
}
