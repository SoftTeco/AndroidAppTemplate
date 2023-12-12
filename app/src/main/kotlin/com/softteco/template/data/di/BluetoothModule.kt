package com.softteco.template.data.di

import com.softteco.template.ui.feature.bluetooth.BluetoothHelper
import com.softteco.template.utils.BluetoothHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface BluetoothModule {

    @Binds
    fun bindBluetoothHelper(bluetoothHelper: BluetoothHelperImpl): BluetoothHelper
}