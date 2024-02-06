package com.softteco.template.data.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Singleton

@Singleton
internal class FirebaseAnalyticsProvider(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsProvider {
    override fun logEvent(eventName: String) {
        firebaseAnalytics.logEvent(eventName, null)
    }

    override fun logEvent(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }
}
