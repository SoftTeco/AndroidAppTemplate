package com.softteco.template.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
) {
    Layout(
        state = viewModel.state.collectAsState().value,
        onProfileClick = onProfileClick,
        modifier = modifier
    )
}

@Composable
private fun Layout(
    state: HomeViewModel.State,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)) {
            Text(state.data)
            Button(onClick = onProfileClick) {
                Text("Details")
            }
        }
    }
}

// region Previews

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Layout(HomeViewModel.State(), onProfileClick = {})
    }
}

// endregion
