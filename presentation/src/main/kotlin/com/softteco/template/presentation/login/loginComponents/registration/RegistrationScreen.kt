package com.softteco.template.presentation.login.loginComponents.registration

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softteco.template.domain.model.DataObject
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.CountryViewModel
import com.softteco.template.presentation.login.loginComponents.*
import com.softteco.template.presentation.login.loginComponents.login.PasswordFieldComponent
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
    val countryList = mutableListOf<String>()

    fun createCountryList(list: List<DataObject>) {
        for (item in list) {
            countryList.add(item.country)
        }
    }

    when (val countriesResponse = countryViewModel.countriesResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> createCountryList(countriesResponse.data.data)
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

            SimpleField(
                fieldName = firstName,
                fieldNameErrorState = firstNameErrorState,
                fieldNameStr = R.string.first_name
            )
            Spacer(Modifier.size(16.dp))

            SimpleField(
                fieldName = lastName,
                fieldNameErrorState = lastNameErrorState,
                fieldNameStr = R.string.last_name
            )
            Spacer(Modifier.size(16.dp))

            EmailFieldComponent(
                fieldName = email,
                fieldNameErrorState = emailErrorState,
                fieldNameStr = R.string.email
            )
            Spacer(Modifier.size(16.dp))
            val passwordVisibility = remember { mutableStateOf(true) }

            PasswordFieldComponent(
                fieldName = password,
                fieldNameErrorState = passwordErrorState,
                passwordVisibility = passwordVisibility
            )
            Spacer(Modifier.size(16.dp))
            val cPasswordVisibility = remember { mutableStateOf(true) }

            PasswordFieldComponent(
                fieldName = confirmPassword,
                fieldNameErrorState = confirmPasswordErrorState,
                passwordVisibility = cPasswordVisibility
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

            if (countryErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }

            Spacer(Modifier.size(16.dp))

            FieldDatePicker(
                fieldNameErrorState = birthDayErrorState,
                fieldNameStr = R.string.birth_day
            )
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
                            firstNameErrorState.value = false
                            lastNameErrorState.value = false
                            emailErrorState.value = false
                            passwordErrorState.value = false
                            confirmPasswordErrorState.value = false
                            countryErrorState.value = false
                            birthDayErrorState.value = false
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
                        firstName.hashCode(),
                        firstName.value.text,
                        lastName.value.text,
                        country.value,
                        birthDay.value.text,
                        email.value.text,
                        password.value.text,
                        ""
                    )
                )
            }
        }
    })
}