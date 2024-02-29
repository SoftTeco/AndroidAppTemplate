package com.softteco.template

import android.content.Intent
import androidx.core.app.RemoteInput
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.ui.feature.settings.PreferencesKeys
import com.softteco.template.ui.theme.ThemeMode
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.shipbook.shipbooksdk.ShipBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: DataStore<Preferences>,
    sessionManager: SessionManager,
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {

    val theme = dataStore.data
        .map { it[PreferencesKeys.THEME_MODE] ?: ThemeMode.SystemDefault.value }
        .stateIn(viewModelScope, SharingStarted.Lazily, ThemeMode.SystemDefault.value)

    val isUserLoggedIn = sessionManager.isUserLoggedIn
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _enteredText = MutableStateFlow("")
    val enteredText = _enteredText.asStateFlow()

    init {
        viewModelScope.launch {
            isUserLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn == true) registerUserInShipBook()
            }
        }
    }

    private fun registerUserInShipBook() {
        viewModelScope.launch(appDispatchers.io) {
            when (val profile = repository.getUser()) {
                is Result.Success -> {
                    val profileData = profile.data
                    ShipBook.registerUser(
                        profileData.id.toString(),
                        profileData.firstName,
                        profileData.fullName(),
                        profileData.email,
                        null,
                        null
                    )
                }
                is Result.Error -> {
                    Timber.e("Error get user data")
                }
            }
        }
    }

    fun processNotificationReply(remoteInput: Intent) {
        viewModelScope.launch(appDispatchers.default) {
            val enteredText = RemoteInput.getResultsFromIntent(remoteInput)
                ?.getString(Constants.NOTIFICATION_REPLY) ?: ""

            _enteredText.value = enteredText
        }
    }
}
