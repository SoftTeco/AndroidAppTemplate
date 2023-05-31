package com.softteco.template.presentation.login.loginComponents.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.Response
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun LoginUserResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val loginResponse = viewModel.loginResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> {
            Toast.makeText(
                LocalContext.current,
                loginResponse.e.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}