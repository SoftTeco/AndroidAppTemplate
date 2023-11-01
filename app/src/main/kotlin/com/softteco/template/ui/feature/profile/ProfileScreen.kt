package com.softteco.template.ui.feature.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.components.SnackBarState
import com.softteco.template.ui.components.TextSnackbarContainer
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens.PaddingLarge
import com.softteco.template.ui.theme.Dimens.PaddingNormal

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onSignatureClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    ScreenContent(
        state = state,
        onSignatureClicked = onSignatureClicked,
        modifier = modifier
    )
}

@Composable
private fun ScreenContent(
    state: ProfileViewModel.State,
    modifier: Modifier = Modifier,
    onSignatureClicked: () -> Unit = {}
) {
    TextSnackbarContainer(
        modifier = modifier,
        snackbarText = stringResource(state.snackbar.textId),
        showSnackbar = state.snackbar.show,
        onDismissSnackbar = state.dismissSnackBar,
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                when (state.profileState) {
                    ProfileViewModel.GetProfileState.Error -> { /* NOOP */
                    }

                    ProfileViewModel.GetProfileState.Loading -> Text(stringResource(R.string.loading))
                    is ProfileViewModel.GetProfileState.Success -> state.profileState.profile.run {
                        Column(Modifier.padding(horizontal = PaddingLarge)) {
                            ProfileDataItem(titleRes = R.string.username_title, value = username)
                            ProfileDataItem(titleRes = R.string.email_title, value = email)
                            ProfileDataItem(titleRes = R.string.first_name_title, value = firstName)
                            ProfileDataItem(titleRes = R.string.last_name_title, value = lastName)
                            ProfileDataItem(titleRes = R.string.birth_data_title, value = birthDate)
                            ProfileDataItem(titleRes = R.string.country_title, value = country)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(PaddingNormal))

                Button(onClick = onSignatureClicked) {
                    Text("To Signature") // string resources should be used
                }
            }
        }
    }
}

@Composable
private fun ProfileDataItem(
    @StringRes titleRes: Int,
    value: String?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(stringResource(titleRes), fontWeight = FontWeight.Bold)
        Text(value ?: "_")
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            ProfileViewModel.State(
                ProfileViewModel.GetProfileState.Success(
                    Profile(
                        id = 1,
                        username = "Jonn",
                        email = "email@gmail.com",
                        createdAt = "2023-10-30 06:58:31.108922"
                    )
                ),
                snackbar = SnackBarState(
                    textId = 0,
                    show = false
                )
            )
        )
    }
}
