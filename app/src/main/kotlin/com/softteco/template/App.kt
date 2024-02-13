package com.softteco.template

import android.app.Application
import com.softteco.template.BuildConfig.SHIP_BOOK_APP_ID
import com.softteco.template.BuildConfig.SHIP_BOOK_APP_KEY
import com.softteco.template.data.analytics.AnalyticsProvider
import com.softteco.template.utils.Analytics
import dagger.hilt.android.HiltAndroidApp
import io.shipbook.shipbooksdk.Log
import io.shipbook.shipbooksdk.ShipBook
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var firebaseAnalyticsProvider: AnalyticsProvider
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ShipBook.addWrapperClass(Timber::class.java.name)
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    Log.message(tag, message, priority, t)
                }
                init {
                    ShipBook.addWrapperClass(this.javaClass.name)
                }
            })
        }
        ShipBook.start(
            this,
            SHIP_BOOK_APP_ID,
            SHIP_BOOK_APP_KEY
        )
        Analytics.init(firebaseAnalyticsProvider)
    }
}
