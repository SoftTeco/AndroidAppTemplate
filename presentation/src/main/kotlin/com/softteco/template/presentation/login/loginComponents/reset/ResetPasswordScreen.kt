package com.softteco.template.presentation.login.loginComponents.reset

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softteco.template.domain.model.user.ResetPasswordDto
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.CustomTopAppBar
import com.softteco.template.presentation.login.loginComponents.password.RestorePasswordResult

@Composable
fun ResetPasswordScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithTopBarForgotPass(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldWithTopBarForgotPass(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    Scaffold(topBar = {
        CustomTopAppBar(navController, stringResource(id = R.string.password_recovery), true)
    }, content = {

        Column(
            modifier = Modifier.padding(10.dp, 0.dp, 20.dp),

            ) {
            Text(
                modifier = Modifier.padding(10.dp, 80.dp, 0.dp),
                text = stringResource(id = R.string.create_password),
                style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Monospace)
            )
            Text(
                modifier = Modifier.padding(10.dp, 30.dp, 0.dp),
                text = stringResource(id = R.string.password_requirement),
                style = TextStyle(fontSize = 16.sp, fontFamily = FontFamily.Monospace)
            )
            Text(
                modifier = Modifier.padding(10.dp, 5.dp, 0.dp),
                text = stringResource(id = R.string.password_requirement_1),
                style = TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            )
            Text(
                modifier = Modifier.padding(10.dp, 5.dp, 0.dp),
                text = stringResource(id = R.string.password_requirement_2),
                style = TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            )
        }
        Column(
            modifier = Modifier.padding(20.dp, 250.dp, 20.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val password = remember { mutableStateOf(TextFieldValue()) }
            val passwordErrorState = remember { mutableStateOf(false) }

            val confirmPassword = remember { mutableStateOf(TextFieldValue()) }
            val confirmPasswordErrorState = remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(20.dp))

            val passwordVisibility = remember { mutableStateOf(true) }
            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    if (passwordErrorState.value) {
                        passwordErrorState.value = false
                    }
                    password.value = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.password))
                },
                isError = passwordErrorState.value,
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility.value = !passwordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "visibility",
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
            )
            if (passwordErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }
            Spacer(Modifier.size(16.dp))

            val cPasswordVisibility = remember { mutableStateOf(true) }
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = {
                    if (confirmPasswordErrorState.value) {
                        confirmPasswordErrorState.value = false
                    }
                    confirmPassword.value = it
                },
                modifier = Modifier.fillMaxWidth(),
                isError = confirmPasswordErrorState.value,
                label = {
                    Text(text = stringResource(id = R.string.confirm_password))
                },
                trailingIcon = {
                    IconButton(onClick = {
                        cPasswordVisibility.value = !cPasswordVisibility.value
                    }) {
                        Icon(
                            imageVector = if (cPasswordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "visibility",
                            tint = Color.Black
                        )
                    }
                },
                visualTransformation = if (cPasswordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
            )
            if (confirmPasswordErrorState.value) {
                val msg = if (confirmPassword.value.text.isEmpty()) {
                    stringResource(id = R.string.required)
                } else if (confirmPassword.value.text != password.value.text) {
                    stringResource(id = R.string.password_not_matching)
                } else {
                    ""
                }
                Text(text = msg, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        when {
                            password.value.text.isEmpty() -> {
                                passwordErrorState.value = true
                            }
                            confirmPassword.value.text.isEmpty() -> {
                                confirmPasswordErrorState.value = true
                            }
                            confirmPassword.value.text != password.value.text -> {
                                confirmPasswordErrorState.value = true
                            }
                            else -> {
                                //authViewModel.restorePassword(ResetPasswordDto())
                            }
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.set_password))
                }
            }
        }
    })
    RestorePasswordResult()
}
