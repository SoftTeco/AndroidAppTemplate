package com.softteco.template.ui.feature.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val profileState = MutableStateFlow(Profile())
    private val greetingState = MutableStateFlow("")
    private val snackbarState = MutableStateFlow(SnackBarState())
    private val loading = MutableStateFlow(true)

    val state = combine(
        loading,
        profileState,
        greetingState,
        snackbarState
    ) { loading, user, greeting, snackbar ->
        State(
            loading = loading,
            profile = user,
            greeting = greeting,
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
            profileRepository.getUser(UUID.randomUUID().toString()).run {
                when (val result = this) {
                    is Result.Success -> profileState.value = result.data
                     is Result.Error -> handleApiError(result, snackbarState)
                }
            }
            profileRepository.getApi().run {
                when (val result = this) {
                    is Result.Success -> greetingState.value = result.data
                    is Result.Error -> handleApiError(result, snackbarState)
                }
            }
            loading.value = false
        }
    }


    @Immutable
    data class State(
        val loading: Boolean = false,
        val profile: Profile = Profile(),
        val greeting: String = "",
        val snackbar: SnackBarState = SnackBarState(),
        val dismissSnackBar: () -> Unit = {},
    )
}
