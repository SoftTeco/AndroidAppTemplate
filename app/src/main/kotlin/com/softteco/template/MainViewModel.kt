package com.softteco.template

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: DataStore<Preferences>,
    appDispatchers: AppDispatchers,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    val theme = dataStore.data
        .map { it[PreferencesKeys.THEME_MODE] ?: ThemeMode.SystemDefault.value }
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.SystemDefault.value)

    val isUserLoggedIn = MutableStateFlow<Boolean?>(null)

    init {
        viewModelScope.launch(appDispatchers.io) {
            profileRepository.getUser().let { result ->
                isUserLoggedIn.value = result.isSuccess
            }
        }
    }
}
