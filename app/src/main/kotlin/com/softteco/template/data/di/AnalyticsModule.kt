package com.softteco.template.data.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.softteco.template.data.analytics.AnalyticsProvider
import com.softteco.template.data.analytics.FirebaseAnalyticsProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalyticsProvider(@ApplicationContext appContext: Context): AnalyticsProvider {
        return FirebaseAnalyticsProvider(FirebaseAnalytics.getInstance(appContext))
    }
}
