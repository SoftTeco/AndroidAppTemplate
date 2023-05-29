package com.softteco.template.presentation.features.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.usecase.apientry.GetApiEntryByNameUseCase
import com.softteco.template.domain.usecase.apientry.ToggleFavoritesUseCase
import com.softteco.template.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getApiEntryByNameUseCase: GetApiEntryByNameUseCase,
    private val toggleFavoritesUseCase: ToggleFavoritesUseCase,
) : BaseViewModel() {

    private val args = ApiDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    internal val state: StateFlow<ApiEntry> = getApiEntryByNameUseCase(args.apiEntry.name)
        .map { it ?: args.apiEntry }
        .stateIn(viewModelScope, SharingStarted.Lazily, args.apiEntry)

    internal fun onToggleFavorite() = viewModelScope.launch {
        toggleFavoritesUseCase(state.value)
    }
}
