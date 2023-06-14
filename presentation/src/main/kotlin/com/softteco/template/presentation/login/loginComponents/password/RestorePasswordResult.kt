package com.softteco.template.presentation.login.loginComponents.password

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog

import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun RestorePasswordResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val restorePasswordResponse = viewModel.restorePasswordApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> {
            CustomAlertDialog(
                onGoToScreen = { /*TODO*/ },
                message = stringResource(id = R.string.check_email)
            )
        }
        is ApiResponse.Failure -> {
            Toast.makeText(
                LocalContext.current, restorePasswordResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}