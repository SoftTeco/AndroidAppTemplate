package com.softteco.template.presentation.common

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

abstract class ComposeFragment : Fragment() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // disable the stretchy overscroll behavior on android S, it causes major issues with nested scrolling
        val overscrollConfig = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) OverscrollConfiguration() else null
        return ComposeView(requireContext()).apply {
            this.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    CompositionLocalProvider(LocalOverscrollConfiguration provides overscrollConfig) {
                        Box(modifier = if (applyStatusBarPadding) Modifier.statusBarsPadding() else Modifier) {
                            this@ComposeFragment.Content()
                        }
                    }
                }
            }
        }
    }

    open val applyStatusBarPadding: Boolean = true

    @Composable
    abstract fun Content()
}
