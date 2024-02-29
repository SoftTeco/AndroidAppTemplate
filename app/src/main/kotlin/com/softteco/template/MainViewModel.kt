package com.softteco.template

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: DataStore<Preferences>,
    sessionManager: SessionManager,
    val appDispatchers: AppDispatchers,
) : ViewModel() {

    val theme = dataStore.data
        .map { it[PreferencesKeys.THEME_MODE] ?: ThemeMode.SystemDefault.value }
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.SystemDefault.value)

    val isUserLoggedIn = sessionManager.isUserLoggedIn
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
}
