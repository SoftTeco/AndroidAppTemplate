package com.softteco.template.presentation.login.loginComponents.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun LoginUserResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val loginResponse = viewModel.loginApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> Unit
        is ApiResponse.Failure -> {
            Toast.makeText(
                LocalContext.current,
                loginResponse.e.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}