package com.softteco.template.data.di

import android.content.Context
import com.polidea.rxandroidble3.RxBleClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @Singleton
    @Provides
    fun provideRxBleClient(@ApplicationContext appContext: Context): RxBleClient = RxBleClient.create(appContext)
}