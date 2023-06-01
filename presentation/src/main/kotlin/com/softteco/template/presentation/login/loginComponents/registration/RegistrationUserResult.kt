package com.softteco.template.presentation.login.loginComponents.registration

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.Response
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.AuthViewModelDb
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun RegistrationUserResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val addUserResponse = viewModel.registerResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> {
            Toast.makeText(
                LocalContext.current,
                addUserResponse.e.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun RegistrationUserResultDb(
    viewModel: AuthViewModelDb = hiltViewModel()
) {
    when (val addUserResponse = viewModel.registerResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            Toast.makeText(
                LocalContext.current,
                "Success",
                Toast.LENGTH_SHORT
            ).show()
        }
        is Response.Failure -> {
            Toast.makeText(
                LocalContext.current,
                addUserResponse.e.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}