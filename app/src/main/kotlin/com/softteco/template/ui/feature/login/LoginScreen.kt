package com.softteco.template.ui.feature.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.data.login.model.LoginAuthDto
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.TextSnackbarContainer


@Composable
fun LoginScreen(
	modifier: Modifier = Modifier,
	viewModel: LoginViewModel = hiltViewModel(),
	onBackClicked: () -> Unit = {},
	onLoginClicked: () -> Unit = {}
) {


	ScreenContent(
		viewModel = viewModel,
		onBackClicked = onBackClicked,
		onLoginClicked = onLoginClicked,
		modifier = modifier
	)
}

@Composable
private fun ScreenContent(
	viewModel: LoginViewModel,
	modifier: Modifier = Modifier,
	onBackClicked: () -> Unit = {},
	onLoginClicked: () -> Unit = {}
) {
	val state by viewModel.state.collectAsState()
	var context = LocalContext.current
	TextSnackbarContainer(
		modifier = Modifier,
		snackbarText = stringResource(state.snackbar.textId),
		showSnackbar = state.snackbar.show,
		onDismissSnackbar = state.dismissSnackBar,
	) {
		Box(modifier.fillMaxSize()) {
			Column {
				CustomTopAppBar(
					modifier = Modifier.fillMaxWidth(),
					stringResource(id = R.string.login),
					showBackIcon = true,
					onBackClicked = onBackClicked
				)
				Column(
					modifier = Modifier.padding(20.dp),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					if (state.loading) {
						Text(stringResource(id = R.string.loading))
					}
					Spacer(modifier = Modifier.height(20.dp))
					OutlinedTextField(
						value = "",
						onValueChange = {},
						modifier = Modifier.fillMaxWidth(),
						label = {
							Text(text = stringResource(id = R.string.email))
						},
					)
					Spacer(
						modifier = Modifier
							.height(20.dp)
							.size(16.dp)
					)

					OutlinedTextField(
						value = "",
						onValueChange = {},
						modifier = Modifier.fillMaxWidth(),
						label = {
							Text(text = stringResource(id = R.string.password))
						},
					)
					Spacer(modifier = Modifier.height(20.dp))
					Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
						Button(
							onClick = {
								viewModel.login(LoginAuthDto("qwerty@mail.ru", "qwerty"))
								if (viewModel.loginState.value) {
									onLoginClicked() //transfer to user's screen
								} else {
									Toast.makeText(
										context, "You have a problem", Toast.LENGTH_SHORT
									).show()
								}
							},
							shape = RoundedCornerShape(50.dp),
							modifier = Modifier
								.fillMaxWidth()
								.height(50.dp)
						) {
							Text(text = stringResource(id = R.string.login))
						}
					}
				}
			}
		}
	}
}