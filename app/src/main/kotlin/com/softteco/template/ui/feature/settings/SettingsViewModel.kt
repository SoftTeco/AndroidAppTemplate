package com.softteco.template.ui.feature.settings

import androidx.compose.runtime.Immutable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.saveToDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Named("themeMode") private val themeModeDataStore: DataStore<Preferences>,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch(appDispatchers.ui) {
            themeModeDataStore.saveToDataStore(PreferencesKeys.THEME_MODE, themeMode.value)
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
