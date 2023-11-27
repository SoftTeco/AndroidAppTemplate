package com.softteco.template.ui.feature.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.utils.AppDispatchers
import com.softteco.template.utils.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val COUNTRY_DEBOUNCE = 600

@OptIn(FlowPreview::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val profileState = MutableStateFlow<GetProfileState>(GetProfileState.Loading)
    private val snackbarState = MutableStateFlow(SnackBarState())
    private val countryState = MutableStateFlow("")
    private val countryList = MutableStateFlow(emptyList<String>())

    val state = combine(
        profileState,
        countryList,
        snackbarState
    ) { profile, countries, snackbar ->
        State(
            profileState = profile,
            snackbar = snackbar,
            dismissSnackBar = { snackbarState.value = SnackBarState() },
            onProfileChanged = { onProfileChanged(it) },
            countries = countries,
            onCountryChanged = { country -> countryState.value = country },
            onLogoutClicked = {
                viewModelScope.launch(appDispatchers.io) {
                    profileRepository.logout()
                    profileState.value = GetProfileState.Logout
                }
            },
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch(appDispatchers.io) {
            profileRepository.getUser().let { result ->
                profileState.value = when (result) {
                    is Result.Success -> GetProfileState.Success(result.data)
                    is Result.Error -> {
                        handleApiError(result, snackbarState)
                        GetProfileState.Error
                    }
                }
            }
        }

        viewModelScope.launch(appDispatchers.io) {
            countryState
                .drop(1)
                .debounce(COUNTRY_DEBOUNCE.milliseconds)
                .map { it.trim() }
                .collect { name ->
                    countryList.value = if (name.isEmpty()) {
                        emptyList()
                    } else {
                        when (val result = profileRepository.getCountryList(name)) {
                            is Result.Success -> result.data
                            else -> emptyList()
                        }
                    }
                }
        }
    }

    private fun onProfileChanged(profile: Profile) {
        viewModelScope.launch(appDispatchers.ui) { profileRepository.cacheProfile(profile) }
        profileState.value.run {
            if (this is GetProfileState.Success) {
                profileState.value = GetProfileState.Success(profile)
            }
        }
    }

    @Immutable
    data class State(
        val profileState: GetProfileState = GetProfileState.Loading,
        val onProfileChanged: (profile: Profile) -> Unit = {},
        val countries: List<String> = emptyList(),
        val onCountryChanged: (String) -> Unit = {},
        val onCountrySelected: (String) -> Unit = {},
        val onLogoutClicked: () -> Unit = {},
        val snackbar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {},
    )

    @Immutable
    sealed class GetProfileState {
        class Success(val profile: Profile) : GetProfileState()
        object Loading : GetProfileState()
        object Error : GetProfileState()
        object Logout : GetProfileState()
    }
}
