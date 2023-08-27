package com.softteco.template.ui.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignatureClicked: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    ScreenContent(
        state = state,
        onSignatureClicked = onSignatureClicked,
        modifier = modifier,
        onDismissSnackbar = { viewModel.dismissSnackbar() }
    )
}

@Composable
private fun ScreenContent(
    state: ProfileViewModel.State,
    modifier: Modifier = Modifier,
    onSignatureClicked: () -> Unit = {},
    onDismissSnackbar: () -> Unit = {},
) {
    TextSnackbarContainer(
        modifier = Modifier,
        snackbarText = stringResource(state.snackbar.textId),
        showSnackbar = state.snackbar.show,
        onDismissSnackbar = onDismissSnackbar,
    ) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)) {
                if (state.loading) {
                    Text("Loading...") // string resources should be used
                } else {
                    Text(state.profile.name)
                    Text(state.greeting)
                }
                Button(onClick = onSignatureClicked) {
                    Text("To Signature") // string resources should be used
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            ProfileViewModel.State(
                profile = Profile(
                    id = "1",
                    name = "Jonn"
                ),
                greeting = "Hello",
                snackbar = SnackBarState(
                    textId = 0,
                    show = false
                )
            )
        )
    }
}
