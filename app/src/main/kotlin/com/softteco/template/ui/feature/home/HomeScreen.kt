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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun HomeScreen(
	viewModel: HomeViewModel = hiltViewModel(),
	onApiSampleClicked: () -> Unit = {},
	onGoLoggingClicked: () -> Unit = {},
) {
	ScreenContent(
		state = viewModel.state.collectAsState().value,
		onApiSampleClicked = { onApiSampleClicked() },
		onGoLoggingClicked = { onGoLoggingClicked() }
	)
}

@Composable
private fun ScreenContent(
	state: HomeViewModel.State,
	modifier: Modifier = Modifier,
	onApiSampleClicked: () -> Unit = {},
	onGoLoggingClicked: () -> Unit = {}
) {
	Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)) {
			Text(state.data)
			Button(onClick = onApiSampleClicked) {
				Text("To Api Sample")
			}
			Button(onClick = onGoLoggingClicked) {
				Text(stringResource(id = R.string.go_to_logging))
			}
		}
	}
}

@Preview
@Composable
private fun Preview() {
	AppTheme {
		ScreenContent(HomeViewModel.State())
	}
}
