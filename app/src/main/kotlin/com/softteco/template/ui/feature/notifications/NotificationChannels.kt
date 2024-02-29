package com.softteco.template.ui.feature.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.softteco.template.Constants.FCM_DEFAULT_CHANNEL_ID
import com.softteco.template.R

object NotificationChannels {
    data class Channel(
        val id: String,
        val displayNameRes: Int,
        val isCreateOnDemand: Boolean = true,
        val importance: Int
    )

    private val FCM_DEFAULT = Channel(
        FCM_DEFAULT_CHANNEL_ID,
        R.string.default_fcm_channel_name,
        false,
        NotificationManager.IMPORTANCE_HIGH
    )

    val channels = listOf(FCM_DEFAULT)
}

fun Application.createNotificationChannels() {
    (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
        NotificationChannels.channels.forEach { config ->
            if (!config.isCreateOnDemand) {
                val channel = NotificationChannel(
                    config.id,
                    getString(config.displayNameRes),
                    config.importance
                )
                channel.description = getString(config.displayNameRes)
                this.createNotificationChannel(channel)
            }
        }
    }
}
