package com.softteco.template.ui.feature

import androidx.compose.runtime.Immutable
import com.softteco.template.navigation.Screen

@Immutable
sealed class ScreenState {
    data object Default : ScreenState()
    data object Loading : ScreenState()
    data object Success : ScreenState()
    data class Navigate(val screen: Screen) : ScreenState()
}
