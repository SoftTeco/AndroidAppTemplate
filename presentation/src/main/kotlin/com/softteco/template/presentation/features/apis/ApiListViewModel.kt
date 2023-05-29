package com.softteco.template.presentation.features.apis

import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.usecase.apientry.FetchApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.GetAllApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.ToggleFavoritesUseCase
import com.softteco.template.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ApiListViewModel @Inject constructor(
    getAllApiEntriesUseCase: GetAllApiEntriesUseCase,
    private val fetchApisUseCase: FetchApiEntriesUseCase,
    private val toggleFavoritesUseCase: ToggleFavoritesUseCase
) : BaseViewModel() {

    internal val apiList = getAllApiEntriesUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _fetchApiListOutput = MutableStateFlow<Output<List<ApiEntry>>?>(null)
    internal val fetchApiListOutput: StateFlow<Output<List<ApiEntry>>?> = _fetchApiListOutput

    internal fun fetchAllApis() = viewModelScope.launch {
        fetchApisUseCase().onEach { _fetchApiListOutput.value = it }.collect()
    }

    internal fun onToggleFavorite(position: Int) = viewModelScope.launch {
        toggleFavoritesUseCase(apiList.value[position])
    }
}
