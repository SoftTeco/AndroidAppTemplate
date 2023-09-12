package com.softteco.template.ui.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ScreenContent(state = state)
}

@Composable
private fun ScreenContent(
    state: SettingsViewModel.State,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
            Text(state.data)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(SettingsViewModel.State())
    }
}
