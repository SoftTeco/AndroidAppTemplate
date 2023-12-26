package com.softteco.template.ui.feature.settings

import androidx.compose.runtime.Immutable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch(appDispatchers.ui) {
            dataStore.edit { it[PreferencesKeys.THEME_MODE] = themeMode.value }
        }
    }

    @Immutable
    data class State(
        val data: String = "Settings"
    )
}

object PreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
}
