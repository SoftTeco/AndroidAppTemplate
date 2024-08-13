package com.softteco.template.ui.feature.navigation.androidauto

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.androidauto.MapboxCarContext
import com.mapbox.androidauto.deeplink.GeoDeeplinkNavigateAction
import com.mapbox.androidauto.map.MapboxCarMapLoader
import com.mapbox.androidauto.map.compass.CarCompassRenderer
import com.mapbox.androidauto.map.logo.CarLogoRenderer
import com.mapbox.androidauto.screenmanager.MapboxScreen
import com.mapbox.androidauto.screenmanager.MapboxScreenManager
import com.mapbox.androidauto.screenmanager.prepareScreens
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.androidauto.mapboxMapInstaller
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.trip.session.TripSessionState
import kotlinx.coroutines.launch

@OptIn(MapboxExperimental::class)
class MainCarSession : Session() {

    private val mapboxCarMapLoader = MapboxCarMapLoader()

    private val mapboxCarMap = mapboxMapInstaller()
        .onCreated(mapboxCarMapLoader)
        .onResumed(CarLogoRenderer(), CarCompassRenderer())
        .install()

    private val mapboxCarContext = MapboxCarContext(lifecycle, mapboxCarMap)
        .prepareScreens()

    private val mapboxNavigation by requireMapboxNavigation()

    init {
        CarAppSyncComponent.getInstance().setCarSession(this)

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                checkLocationPermissions()
                observeAutoDrive()
            }
        })
    }

    override fun onCreateScreen(intent: Intent): Screen {
        val screenKey = MapboxScreenManager.current()?.key
        checkNotNull(screenKey) { "The screen key should be set before the Screen is requested." }
        return mapboxCarContext.mapboxScreenManager.createScreen(screenKey)
    }

    override fun onCarConfigurationChanged(newConfiguration: Configuration) {
        mapboxCarMapLoader.onCarConfigurationChanged(carContext)
    }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (PermissionsManager.areLocationPermissionsGranted(carContext)) {
            GeoDeeplinkNavigateAction(mapboxCarContext).onNewIntent(intent)
        }
    }

    private fun checkLocationPermissions() {
        PermissionsManager.areLocationPermissionsGranted(carContext).also { isGranted ->
            val currentKey = MapboxScreenManager.current()?.key
            if (!isGranted) {
                MapboxScreenManager.replaceTop(MapboxScreen.NEEDS_LOCATION_PERMISSION)
            } else if (currentKey == null || currentKey == MapboxScreen.NEEDS_LOCATION_PERMISSION) {
                MapboxScreenManager.replaceTop(MapboxScreen.FREE_DRIVE)
            }
        }
    }

    private fun observeAutoDrive() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mapboxCarContext.mapboxNavigationManager.autoDriveEnabledFlow.collect {
                    refreshTripSession()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun refreshTripSession() {
        if (!PermissionsManager.areLocationPermissionsGranted(carContext)) {
            mapboxNavigation.stopTripSession()
            return
        }
        if (mapboxNavigation.getTripSessionState() != TripSessionState.STARTED) {
            mapboxNavigation.startTripSession()
        }
    }
}
