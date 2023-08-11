package com.softteco.template.presentation.login.loginComponents.password


import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel

import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun RestorePasswordResult(
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    when (val restorePasswordResponse = viewModel.restorePasswordApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> Toast.makeText(
            context, context.getString(R.string.check_email), Toast.LENGTH_LONG
        ).show()
        is ApiResponse.Failure -> {
            Toast.makeText(
                context, restorePasswordResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}