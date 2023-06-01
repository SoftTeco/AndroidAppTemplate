package com.softteco.template.presentation.login.loginComponents.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softteco.template.domain.model.user.Account
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModelDb
import com.softteco.template.presentation.login.loginComponents.CustomTopAppBar


@Composable
fun RegistrationScreen(navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithTopBar(navController)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopBar(navController: NavHostController) {

    val authViewModel: AuthViewModelDb = hiltViewModel()

    Scaffold(topBar = {
        CustomTopAppBar(navController, stringResource(id = R.string.sign_up), true)
    }, content = {

        val firstName = remember {
            mutableStateOf(TextFieldValue())
        }
        val lastName = remember {
            mutableStateOf(TextFieldValue())
        }
        val email = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val confirmPassword = remember { mutableStateOf(TextFieldValue()) }
        val country = remember { mutableStateOf(TextFieldValue()) }
        val birthDay = remember { mutableStateOf(TextFieldValue()) }

        val firstNameErrorState = remember { mutableStateOf(false) }
        val lastNameErrorState = remember { mutableStateOf(false) }
        val emailErrorState = remember { mutableStateOf(false) }
        val passwordErrorState = remember { mutableStateOf(false) }
        val confirmPasswordErrorState = remember { mutableStateOf(false) }
        val countryErrorState = remember { mutableStateOf(false) }
        val birthDayErrorState = remember { mutableStateOf(false) }

        val scrollState = rememberScrollState()
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Monospace)
            )

            Spacer(Modifier.size(16.dp))
            OutlinedTextField(
                value = firstName.value,
                onValueChange = {
                    if (firstNameErrorState.value) {
                        firstNameErrorState.value = false
                    }
                    firstName.value = it
                },

                modifier = Modifier.fillMaxWidth(),
                isError = firstNameErrorState.value,
                label = {
                    Text(text = stringResource(id = R.string.first_name))
                },
            )
            if (firstNameErrorState.value) {
                Text(text = stringResource(id = R.string.first_name), color = Color.Red)
            }
            Spacer(Modifier.size(16.dp))

            OutlinedTextField(
                value = lastName.value,
                onValueChange = {
                    if (lastNameErrorState.value) {
                        lastNameErrorState.value = false
                    }
                    lastName.value = it
                },

                modifier = Modifier.fillMaxWidth(),
                isError = lastNameErrorState.value,
                label = {
                    Text(text = stringResource(id = R.string.last_name))
                },
            )
            if (lastNameErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }
            Spacer(Modifier.size(16.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    if (emailErrorState.value) {
                        emailErrorState.value = false
                    }
                    email.value = it
                },

                modifier = Modifier.fillMaxWidth(),
                isError = emailErrorState.value,
                label = {
                    Text(text = stringResource(id = R.string.email))
                },
            )
            if (emailErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }

            Spacer(Modifier.size(16.dp))
            val passwordVisibility = remember { mutableStateOf(true) }
            OutlinedTextField(value = password.value,
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
            OutlinedTextField(value = confirmPassword.value,
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
            Spacer(Modifier.size(16.dp))
            OutlinedTextField(
                value = country.value,
                onValueChange = {
                    if (countryErrorState.value) {
                        countryErrorState.value = false
                    }
                    country.value = it
                },

                modifier = Modifier.fillMaxWidth(),
                isError = emailErrorState.value,
                label = {
                    Text(text = stringResource(id = R.string.country))
                },
            )
            if (emailErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }

            Spacer(Modifier.size(16.dp))
            OutlinedTextField(
                value = birthDay.value,
                onValueChange = {
                    if (birthDayErrorState.value) {
                        birthDayErrorState.value = false
                    }
                    birthDay.value = it
                },

                modifier = Modifier.fillMaxWidth(),
                isError = emailErrorState.value,
                label = {
                    Text(text = stringResource(id = R.string.birth_day))
                },
            )
            if (emailErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }

            Spacer(Modifier.size(16.dp))
            Button(
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    when {
                        firstName.value.text.isEmpty() -> {
                            firstNameErrorState.value = true
                        }
                        lastName.value.text.isEmpty() -> {
                            lastNameErrorState.value = true
                        }
                        email.value.text.isEmpty() -> {
                            emailErrorState.value = true
                        }
                        password.value.text.isEmpty() -> {
                            passwordErrorState.value = true
                        }
                        confirmPassword.value.text.isEmpty() -> {
                            confirmPasswordErrorState.value = true
                        }
                        confirmPassword.value.text != password.value.text -> {
                            confirmPasswordErrorState.value = true
                        }
                        country.value.text.isEmpty() -> {
                            countryErrorState.value = true
                        }
                        birthDay.value.text.isEmpty() -> {
                            birthDayErrorState.value = true
                        }
                        else -> {
                            authViewModel.register(
                                Account(
                                    1,
                                    firstName.value.text,
                                    lastName.value.text,
                                    country.value.text,
                                    birthDay.value.text,
                                    email.value.text,
                                    password.value.text,
                                    "",
                                )
                            )
                        }
                    }
                },
                content = {
                    Text(text = stringResource(id = R.string.sign_up), color = Color.White)
                },
            )
            RegistrationUserResultDb()
        }
    })
}