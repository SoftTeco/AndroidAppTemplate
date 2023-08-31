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
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.data.login.model.LoginAuthDto
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.PasswordField
import com.softteco.template.ui.components.SimpleField
import com.softteco.template.ui.theme.Dimens

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {}
) {
    ScreenContent(
        onBackClicked = onBackClicked,
        onLoginClicked = onLoginClicked,
        onSignUpClicked = onSignUpClicked,
        modifier = modifier
    )
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {}
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val emailError by viewModel.fieldValidationError.collectAsState()

    Box(modifier.fillMaxSize()) {
        Column {
            CustomTopAppBar(
                stringResource(id = R.string.login),
                showBackIcon = true,
                modifier = Modifier.fillMaxWidth(),
                onBackClicked = onBackClicked
            )
            Column(
                modifier = Modifier.padding(Dimens.Padding20),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.loading) {
                    Text(stringResource(id = R.string.loading))
                }
                Spacer(modifier = Modifier.height(Dimens.Padding20))

                SimpleField(
                    strId = R.string.email,
                    email,
                    fieldErrorState = email.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    onFieldValueChanged = { newValue ->
                        email = newValue
                        viewModel.changeEmail(newValue)
                    }
                )

                if (email.isNotEmpty() && !emailError.isEmailCorrect) {
                    Text(
                        text = stringResource(id = R.string.email_not_valid),
                        color = Color.Red
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(Dimens.Padding20)
                        .size(Dimens.PaddingNormal)
                )

                PasswordField(
                    strId = R.string.password,
                    password,
                    nameErrorState = password.isEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    onNameChanged = { newValue -> password = newValue }
                )

                Spacer(modifier = Modifier.height(Dimens.Padding20))
                Box(
                    modifier = Modifier.padding(
                        Dimens.Padding40,
                        Dimens.Padding0,
                        Dimens.Padding40,
                        Dimens.Padding0
                    )
                ) {
                    Button(
                        onClick = {
                            if (!emailError.isEmailCorrect ||
                                email.isEmpty() || password.isEmpty()
                            ) {
                                Toast.makeText(
                                    context,
                                    context.getText(R.string.empty_fields_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.login(LoginAuthDto(email, password))
                                if (viewModel.loginState.value) {
                                    onLoginClicked() // transfer to user's screen
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getText(R.string.problem_error),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        shape = RoundedCornerShape(Dimens.Padding50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.Padding50)
                    ) {
                        Text(text = stringResource(id = R.string.login))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.sign_up)),
                    modifier = Modifier
                        .padding(Dimens.Padding20),
                    onClick = {
                        onSignUpClicked()
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
}
