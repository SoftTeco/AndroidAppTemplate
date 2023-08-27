package com.softteco.template.ui.feature.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.R
import com.softteco.template.data.base.error.ErrorEntity
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.components.SnackBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
            snackbar = snackbar
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
                    is Result.Error -> handleError(result.error)
                }
            }
            profileRepository.getApi().run {
                when (val result = this) {
                    is Result.Success -> greetingState.value = result.data
                    is Result.Error -> handleError(result.error)
                }
            }
            loading.value = false
        }
    }

    private fun handleError(error: ErrorEntity) {
        if (error.isDisplayable) {
            val textId = when (error) {
                ErrorEntity.AccessDenied -> R.string.error_example
                ErrorEntity.Network -> R.string.error_example
                ErrorEntity.NotFound -> R.string.error_example
                ErrorEntity.ServiceUnavailable -> R.string.error_example
                ErrorEntity.Unknown -> R.string.error_example
            }
            snackbarState.value = SnackBarState(
                textId = textId,
                show = true,
            )
        }
    }

    fun dismissSnackbar() = snackbarState.update { SnackBarState() }

    @Immutable
    data class State(
        val loading: Boolean = true,
        val profile: Profile = Profile(),
        val greeting: String = "",
        val snackbar: SnackBarState = SnackBarState(),
    )
}
