package com.softteco.template.ui.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar

@Composable
fun LoginScreen(
	onBackClicked: () -> Unit = {},
	onLoginClicked: () -> Unit = {}
) {
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
					onClick = { onLoginClicked() },
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