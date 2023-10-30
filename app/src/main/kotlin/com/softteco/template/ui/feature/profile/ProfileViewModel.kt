package com.softteco.template.ui.feature.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.utils.handleApiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val profileState = MutableStateFlow<GetProfileState>(GetProfileState.Loading)
    private val snackbarState = MutableStateFlow(SnackBarState())

    val state = combine(
        profileState,
        snackbarState
    ) { profile, snackbar ->
        State(
            profileState = profile,
            snackbar = snackbar,
            dismissSnackBar = { snackbarState.value = SnackBarState() }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        State()
    )

    init {
        viewModelScope.launch {
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
    }

    @Immutable
    data class State(
        val profileState: GetProfileState = GetProfileState.Loading,
        val snackbar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {},
    )

    @Immutable
    sealed class GetProfileState {
        class Success(val profile: Profile) : GetProfileState()
        object Loading : GetProfileState()
        object Error : GetProfileState()
    }
}
