package com.softteco.template.presentation.login.loginComponents.password


import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog

import com.softteco.template.presentation.login.loginComponents.ProgressBar
import com.softteco.template.presentation.login.loginComponents.Routes

@Composable
fun RestorePasswordResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    when (val restorePasswordResponse = viewModel.restorePasswordApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> {
            CustomAlertDialog(
                onGoToScreen = { },
                message = context.getString(R.string.set_password_success)
            )
        }
        is ApiResponse.Failure -> {
            Toast.makeText(
                LocalContext.current, restorePasswordResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}