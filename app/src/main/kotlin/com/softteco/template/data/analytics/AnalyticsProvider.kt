package com.softteco.template.data.analytics

import android.os.Bundle

interface AnalyticsProvider {

    fun logEvent(eventName: String)

    fun logEvent(eventName: String, params: Bundle)
}
