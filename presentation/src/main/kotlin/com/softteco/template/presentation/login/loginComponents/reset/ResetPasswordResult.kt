package com.softteco.template.presentation.login.loginComponents.reset

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDirections
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.RegistrationComposeFragmentDirections
import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun ResetPasswordResult(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: (NavDirections) -> Unit
) {
    val context = LocalContext.current
    when (val resetPasswordResponse = viewModel.resetPasswordApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> {
            CustomAlertDialog(
                onGoToScreen = {
                    onNavigateToLogin(
                        RegistrationComposeFragmentDirections
                            .actionRegistrationComposeFragmentToLoginComposeFragment()
                    )
                },
                message = context.getString(R.string.reset_password_success)
            )
        }
        is ApiResponse.Failure -> {
            Toast.makeText(
                LocalContext.current, resetPasswordResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}