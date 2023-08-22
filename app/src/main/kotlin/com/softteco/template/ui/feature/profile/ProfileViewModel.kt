package com.softteco.template.ui.feature.profile

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.entity.Profile
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
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val profileState = MutableStateFlow<Profile?>(null)
    private val greetingState = MutableStateFlow<String?>(null)
    private val snackbarState = MutableStateFlow<Int?>(null)

    val state = combine(profileState, greetingState, snackbarState) { user, greeting, snackbar ->
        mapToState(user, greeting, snackbar)
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        mapToState(profileState.value, greetingState.value, snackbarState.value)
    )

    init {
        viewModelScope.launch {
            profileRepository.getUser(UUID.randomUUID().toString()).run {
                when (val result = this) {
                    is Result.Success -> profileState.value = result.data
                    is Result.Error -> handleError(result.error)
                }
            }
            profileRepository.getApi().run {
                when (val result = this) {
                    is Result.Success -> greetingState.value = result.data
                    is Result.Error -> handleError(result.error)
                }
            }
        }
    }

    private fun mapToState(
        profile: Profile?,
        greeting: String?,
        showSnackbar: Int?
    ): State {
        return State(
            profile,
            greeting,
            showSnackbar,
            onDismissSnackbar = { snackbarState.value = null }
        )
    }

    private fun handleError(error: ErrorEntity) {
        if (error.isDisplayable) {
            snackbarState.value = when (error) {
                ErrorEntity.AccessDenied -> R.string.error_example
                ErrorEntity.Network -> R.string.error_example
                ErrorEntity.NotFound -> R.string.error_example
                ErrorEntity.ServiceUnavailable -> R.string.error_example
                ErrorEntity.Unknown -> R.string.error_example
            }
        }
    }

    @Immutable
    data class State(
        val profile: Profile?,
        val greeting: String?,
        @StringRes
        val showSnackbar: Int? = null,
        val onDismissSnackbar: () -> Unit,
    )
}
