package com.softteco.template.ui.feature.apisample

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ApiSampleViewModel @Inject constructor() : ViewModel() {

    val state = MutableStateFlow(State())

    @Immutable
    data class State(
        val data: String = "Api Sample data",
    )
}
