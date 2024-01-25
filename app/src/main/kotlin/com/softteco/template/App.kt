package com.softteco.template

import android.app.Application
import com.softteco.template.data.analytics.AnalyticsProvider
import com.softteco.template.utils.Analytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var firebaseAnalyticsProvider: AnalyticsProvider
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Analytics.init(firebaseAnalyticsProvider)
    }
}
