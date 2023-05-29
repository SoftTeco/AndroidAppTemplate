package com.softteco.template.presentation.features.favorites

import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.repository.ApisRepository
import com.softteco.template.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    apiRepository: ApisRepository,
) : BaseViewModel() {

    val state: StateFlow<List<ApiEntry>> =
        apiRepository.favorites.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
