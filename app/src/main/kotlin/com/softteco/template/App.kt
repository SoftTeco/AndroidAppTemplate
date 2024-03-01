package com.softteco.template

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softteco.template.BuildConfig.SHIP_BOOK_APP_ID
import com.softteco.template.BuildConfig.SHIP_BOOK_APP_KEY
import com.softteco.template.data.analytics.AnalyticsProvider
import com.softteco.template.ui.feature.notifications.createNotificationChannels
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.utils.Analytics
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.getFromDataStore
import com.softteco.template.utils.saveToDataStore
import dagger.hilt.android.HiltAndroidApp
import io.shipbook.shipbooksdk.Log
import io.shipbook.shipbooksdk.ShipBook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var firebaseAnalyticsProvider: AnalyticsProvider

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    lateinit var appDispatchers: AppDispatchers

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

        checkAppUpdate()
    }

    private fun checkAppUpdate() {
        CoroutineScope(appDispatchers.io).launch {
            val version = dataStore
                .getFromDataStore(PreferencesKeys.BUILD_VERSION_CODE, 1)
                .first()
            if (version != BuildConfig.VERSION_CODE) {
                createNotificationChannels()

                dataStore.saveToDataStore(
                    PreferencesKeys.BUILD_VERSION_CODE,
                    BuildConfig.VERSION_CODE
                )
            }
        }
    }
}
