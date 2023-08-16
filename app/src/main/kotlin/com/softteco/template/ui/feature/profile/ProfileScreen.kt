package com.softteco.template.ui.feature.profile

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
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens
import com.softteco.template.ui.widgets.TextSnackbarContainer

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    Layout(viewModel.state.collectAsState().value, onBackClick, modifier)
}

@Composable
private fun Layout(
    state: ProfileViewModel.State,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextSnackbarContainer(
        modifier = Modifier,
        snackbarText = stringResource(state.showSnackbar ?: android.R.string.unknownName),
        showSnackbar = state.showSnackbar != null,
        onDismissSnackbar = { state.onDismissSnackbar() }
    ) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)) {
                state.profile?.let { Text(it.name) }
                state.greeting?.let { Text(it) }
                Button(onClick = onBackClick) {
                    Text("Back")
                }
            }
        }
    }
}

// region Previews

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Layout(
            ProfileViewModel.State(Profile("", "John"), "Hi", onDismissSnackbar = {}),
            onBackClick = {}
        )
    }
}

// endregion
