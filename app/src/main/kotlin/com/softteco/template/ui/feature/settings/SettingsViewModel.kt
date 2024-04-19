package com.softteco.template.ui.feature.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.navigation.Screen
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val _navDestination = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navDestination = _navDestination.asSharedFlow().distinctUntilChanged()

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch(appDispatchers.ui) {
            dataStore.edit { it[PreferencesKeys.THEME_MODE] = themeMode.value }
        }
    }

    fun onNavClick(screen: Screen) = _navDestination.tryEmit(screen)
}

object PreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
}
