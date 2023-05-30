package com.softteco.template.presentation.login.loginComponents.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.presentation.login.AuthViewModel


@Composable
fun RegistrationScreen() {
    // State for form fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }

    // State for error messages
    val firstNameError by remember { mutableStateOf("") }
    val lastNameError by remember { mutableStateOf("") }
    val confirmPasswordError by remember { mutableStateOf("") }
    val countryError by remember { mutableStateOf("") }
    val birthdayError by remember { mutableStateOf("") }
    val emailError by remember { mutableStateOf("") }
    val passwordError by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            isError = firstNameError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (firstNameError.isNotEmpty()) {
            Text(
                text = firstNameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            isError = lastNameError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (lastNameError.isNotEmpty()) {
            Text(
                text = lastNameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = emailError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = passwordError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") },
            isError = confirmPasswordError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (confirmPasswordError.isNotEmpty()) {
            Text(
                text = confirmPasswordError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            isError = countryError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (countryError.isNotEmpty()) {
            Text(
                text = countryError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        OutlinedTextField(
            value = birthday,
            onValueChange = { birthday = it },
            label = { Text("Birthday") },
            isError = birthdayError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (birthdayError.isNotEmpty()) {
            Text(
                text = birthdayError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
        Button(
            onClick = {
                authViewModel.register(
                    CreateUserDto(
                    firstName,
                    lastName,
                    email,
                    password,
                    confirmPassword,
                    country,
                    birthday
                )
                )
            },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(text = "Register")
        }
        RegistrationUserResult()
    }
}