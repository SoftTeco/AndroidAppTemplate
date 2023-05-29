package com.softteco.template.presentation.features.splash

import androidx.lifecycle.viewModelScope
import com.softteco.template.domain.model.Output
import com.softteco.template.domain.repository.ApisRepository
import com.softteco.template.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Android Questions
 */

@HiltViewModel
class SplashViewModel @Inject constructor(
    apisRepository: ApisRepository,
) : BaseViewModel() {

    private val _isOk = MutableStateFlow<Output<Boolean>>(Output.loading())
    internal val isOk: StateFlow<Output<Boolean>> = _isOk

    init {
        apisRepository.refresh()
        load()
    }

    /**
     * Method to show splash screen for few milliseconds to user.
     */
    internal fun load(timeMillis: Long = SPLASH_TIME) {
        _isOk.value = Output.loading()
        viewModelScope.launch {
            delay(timeMillis = timeMillis)
            _isOk.value = Output.success(true)
        }
    }

    companion object {
        const val SPLASH_TIME = 800L
    }
}
