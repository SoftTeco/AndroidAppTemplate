package com.softteco.template.ui.feature.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.auth.repository.AuthRepository
import com.softteco.template.data.base.error.AppError.AuthError.InvalidToken
import com.softteco.template.data.base.error.AppError.LocalStorageAppError.AuthTokenNotFound
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.data.profile.entity.toUpdateUserDto
import com.softteco.template.data.profile.repository.ProfileRepository
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

private const val COUNTRY_DEBOUNCE = 600

@OptIn(FlowPreview::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val appDispatchers: AppDispatchers,
    private val snackbarController: SnackbarController,
) : ViewModel() {

    private val profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    private val countryState = MutableStateFlow("")
    private val countryList = MutableStateFlow(emptyList<String>())
    private val originProfile = MutableStateFlow<Profile?>(null)

    val state = combine(
        profileState,
        originProfile,
        countryList,
    ) { profileState, originProfile, countries ->
        State(
            profileState = profileState,
            originProfile = originProfile,
            onProfileChanged = ::onProfileChanged,
            countries = countries,
            onCountryChanged = { country -> countryState.value = country },
            onLogoutClicked = ::logout,
            onSave = { save(profileState) }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch(appDispatchers.io) {
            profileRepository.getUser().first().let { result ->
                profileState.value = when (result) {
                    is Result.Success -> {
                        originProfile.value = result.data
                        ProfileState.Success(result.data)
                    }

                    is Result.Error -> {
                        snackbarController.showSnackbar(result.error.messageRes)
                        if (result.error == InvalidToken || result.error == AuthTokenNotFound) {
                            // could be moved to more proper place,
                            // will be resolved as separate feature
                            authRepository.logout()
                            ProfileState.Logout
                        } else {
                            ProfileState.Error
                        }
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

    private fun logout() {
        viewModelScope.launch(appDispatchers.io) {
            authRepository.logout()
            profileState.value = ProfileState.Logout
        }
    }

    private fun save(currentState: ProfileState) {
        if (currentState is ProfileState.Success) {
            viewModelScope.launch(appDispatchers.io) {
                val profile = currentState.profile
                profileState.value = ProfileState.Saving(profile)
                when (val result = profileRepository.updateUser(profile.toUpdateUserDto())) {
                    is Result.Success -> {
                        profileRepository.cacheProfile(result.data)
                        profileState.value = ProfileState.Success(result.data)
                        originProfile.value = result.data
                    }

                    is Result.Error -> {
                        originProfile.value?.let { profileState.value = ProfileState.Success(it) }
                        snackbarController.showSnackbar(result.error.messageRes)
                    }
                }
            }
        }
    }

    private fun onProfileChanged(profile: Profile) = profileState.update {
        if (it is ProfileState.Success) ProfileState.Success(profile) else it
    }

    @Immutable
    data class State(
        val profileState: ProfileState = ProfileState.Loading,
        val originProfile: Profile? = null,
        val onProfileChanged: (profile: Profile) -> Unit = {},
        val onSave: (profile: Profile) -> Unit = {},
        val countries: List<String> = emptyList(),
        val onCountryChanged: (String) -> Unit = {},
        val onCountrySelected: (String) -> Unit = {},
        val onLogoutClicked: () -> Unit = {},
    )

    @Immutable
    sealed interface ProfileState {
        data class Success(val profile: Profile) : ProfileState
        data object Loading : ProfileState
        data object Error : ProfileState
        data object Logout : ProfileState
        data class Saving(val profile: Profile) : ProfileState
    }
}

fun ProfileViewModel.State.hasProfileChanged(): Boolean {
    // check if profile has been changed
    return profileState is ProfileViewModel.ProfileState.Success &&
        originProfile != null &&
        profileState.profile != originProfile
}

fun ProfileViewModel.State.getProfile(): Profile? {
    return profileState.run {
        when (this) {
            is ProfileViewModel.ProfileState.Success -> profile
            is ProfileViewModel.ProfileState.Saving -> profile
            else -> null
        }
    }
}

fun ProfileViewModel.State.isProfileLoaded(): Boolean {
    return getProfile() != null
}

fun ProfileViewModel.ProfileState.saving(): Boolean {
    return this is ProfileViewModel.ProfileState.Saving
}
