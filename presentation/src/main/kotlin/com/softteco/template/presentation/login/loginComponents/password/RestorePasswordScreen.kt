package com.softteco.template.presentation.login.loginComponents.password

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.ForgotPasswordDto

import com.softteco.template.presentation.login.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestorePasswordScreen() {
    // State for form fields
    var email by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { authViewModel.restorePassword(ForgotPasswordDto(email)) },
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(text = "Restore password")
        }
        RestorePasswordResult()
    }
}