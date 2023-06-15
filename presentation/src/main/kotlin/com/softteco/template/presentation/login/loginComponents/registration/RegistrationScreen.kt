package com.softteco.template.presentation.login.loginComponents.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.CountryViewModel
import com.softteco.template.presentation.login.loginComponents.CustomTopAppBar
import com.softteco.template.presentation.login.loginComponents.DropDownListComponent
import com.softteco.template.presentation.login.loginComponents.ProgressBar
import java.util.*


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

    var signUp by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = hiltViewModel()
    val countryViewModel: CountryViewModel = hiltViewModel()
    var countryList = listOf("Belarus", "USA")

    when (val countriesResponse = countryViewModel.countriesResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> countryList = listOf(countriesResponse.data.data.toString()) //TODO
        is ApiResponse.Failure -> print(countriesResponse.e)
    }

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
        val country = remember { mutableStateOf("") }
        val birthDay = remember { mutableStateOf(TextFieldValue()) }

        val firstNameErrorState = remember { mutableStateOf(false) }
        val lastNameErrorState = remember { mutableStateOf(false) }
        val emailErrorState = remember { mutableStateOf(false) }
        val passwordErrorState = remember { mutableStateOf(false) }
        val confirmPasswordErrorState = remember { mutableStateOf(false) }
        val countryErrorState = remember { mutableStateOf(false) }
        val birthDayErrorState = remember { mutableStateOf(false) }

        val scrollState = rememberScrollState()

        val isOpen = remember { mutableStateOf(false) }
        val openCloseOfDropDownList: (Boolean) -> Unit = {
            isOpen.value = it
        }
        val userSelectedString: (String) -> Unit = {
            country.value = it
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 100.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {

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
            Spacer(Modifier.size(16.dp))


            Box {
                Column {
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
                    DropDownListComponent(
                        requestToOpen = isOpen.value,
                        list = countryList,
                        openCloseOfDropDownList,
                        userSelectedString
                    )
                }
                Spacer(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Transparent)
                        .padding(10.dp)
                        .clickable(onClick = { isOpen.value = true })
                )
            }

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
                        country.value.isEmpty() -> {
                            countryErrorState.value = true
                        }
                        birthDay.value.text.isEmpty() -> {
                            birthDayErrorState.value = true
                        }
                        else -> {
                            authViewModel.register(
                                CreateUserDto(
                                    firstName.value.text,
                                    lastName.value.text,
                                    email.value.text,
                                    password.value.text,
                                    confirmPassword.value.text,
                                    country.value,
                                    birthDay.value.text
                                )
                            )
                        }
                    }
                    signUp = true
                },
                content = {
                    Text(text = stringResource(id = R.string.sign_up), color = Color.White)
                },
            )
            if (signUp) {
                RegistrationUserResult(
                    hiltViewModel(), Account(
                        firstName.hashCode(), firstName.value.text,
                        lastName.value.text, country.value,
                        birthDay.value.text, email.value.text, password.value.text, ""
                    )
                )
            }
        }
    })
}