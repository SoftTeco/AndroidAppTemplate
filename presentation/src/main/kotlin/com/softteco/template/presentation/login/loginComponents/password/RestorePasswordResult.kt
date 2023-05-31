package com.softteco.template.presentation.login.loginComponents.password

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.Response
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun RestorePasswordResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val restorePasswordResponse = viewModel.restorePasswordResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            Toast.makeText(
                LocalContext.current, stringResource(id = R.string.check_email), Toast.LENGTH_SHORT
            ).show()
        }
        is Response.Failure -> {
            Toast.makeText(
                LocalContext.current, restorePasswordResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}