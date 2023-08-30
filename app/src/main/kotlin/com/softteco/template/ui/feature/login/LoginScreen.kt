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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softteco.template.R
import com.softteco.template.ui.components.CustomTopAppBar

@Composable
fun LoginScreen (
	onBackClicked: () -> Unit = {},
	onLoginClicked: () -> Unit = {}
){
	var logIn by remember { mutableStateOf(false) }

//	fun isValidEmail(email: String): Boolean {
//		val emailRegex = Constants.EMAIL_PATTERN.toRegex()
//		return email.matches(emailRegex)
//	}
	Column{
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
			val email = remember { mutableStateOf(TextFieldValue()) }
			val emailErrorState = remember { mutableStateOf(false) }
			val passwordErrorState = remember { mutableStateOf(false) }
			val password = remember { mutableStateOf(TextFieldValue()) }

			Spacer(modifier = Modifier.height(20.dp))
			OutlinedTextField(
				value = email.value,
				onValueChange = {
					if (emailErrorState.value) {
						emailErrorState.value = false
					}
					email.value = it
				},
				isError = emailErrorState.value,
				modifier = Modifier.fillMaxWidth(),
				label = {
					Text(text = stringResource(id = R.string.email))
				},
			)
//			if (emailErrorState.value) {
//				Text(text = stringResource(id = R.string.required), color = Color.Red)
//			}
//			if (email.value.text.isNotEmpty() && !isValidEmail(email.value.text)) {
//				Text(text = stringResource(R.string.email_error), color = Color.Red)
//			}

			Spacer(modifier = Modifier.height(20.dp))
			Spacer(Modifier.size(16.dp))

//			SimplePasswordField(
//				isError = passwordErrorState.value,
//				passwordValue = password.value.text,
//				modifier = Modifier.fillMaxWidth()
//			)
			Spacer(modifier = Modifier.height(20.dp))
			Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
				Button(
					onClick = {
						when {
							email.value.text.isEmpty() -> {
								emailErrorState.value = true
							}

							password.value.text.isEmpty() -> {
								passwordErrorState.value = true
							}

							else -> {
//								logIn = true
//								passwordErrorState.value = false
//								emailErrorState.value = false
//								authViewModel.login(
//									LoginAuthDto(
//										email.value.text,
//										password.value.text
//									)
//								)
							}
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
//			if (logIn) {
//				LoginUserResult()
//			}
			Spacer(modifier = Modifier.height(20.dp))
			ClickableText(
				text = AnnotatedString(stringResource(id = R.string.email)),
				onClick = {
					// TODO - go to Forgot Password
				},
				style = TextStyle(
					fontSize = 14.sp,
					fontFamily = FontFamily.Default
				)
			)
			Spacer(modifier = Modifier.weight(1f))
			ClickableText(
				text = AnnotatedString(stringResource(id = R.string.login)),
				modifier = Modifier
					.padding(20.dp),
				onClick = {

				},
				style = TextStyle(
					fontSize = 20.sp,
					fontFamily = FontFamily.Default,
					textDecoration = TextDecoration.Underline,
					color = Color.Blue
				)
			)
		}
	}
}