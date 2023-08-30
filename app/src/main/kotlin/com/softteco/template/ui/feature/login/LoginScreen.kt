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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.data.login.model.LoginAuthDto
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.SimpleField
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
	val context = LocalContext.current
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

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

					SimpleField(
						modifier = Modifier.fillMaxWidth(),
						strId = R.string.email,
						email,
						nameErrorState = email.isEmpty(),
						onNameChanged = { newValue -> email = newValue })
					Spacer(
						modifier = Modifier
							.height(20.dp)
							.size(16.dp)
					)
					PasswordField(
						modifier = Modifier.fillMaxWidth(),
						strId = R.string.password,
						password,
						nameErrorState = password.isEmpty(),
						onNameChanged = { newValue -> password= newValue })

					Spacer(modifier = Modifier.height(20.dp))
					Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
						Button(
							onClick = {
								viewModel.login(LoginAuthDto(email, password))
								if (viewModel.loginState.value) {
									onLoginClicked() //transfer to user's screen
								} else {
									Toast.makeText(
										context, "Yoy have a problem", Toast.LENGTH_SHORT
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