package com.softteco.template.presentation.login.loginComponents.reset

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.AuthViewModelDb
import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun ResetPasswordResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val resetPasswordResponse = viewModel.resetPasswordApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> {
CustomAlertDialog(onGoToScreen = { /*TODO*/ }, message = stringResource(id = R.string.set_password_success))
        }
        is ApiResponse.Failure -> {
            Toast.makeText(
                LocalContext.current, resetPasswordResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun ResetPasswordDb(viewModelDb: AuthViewModelDb = hiltViewModel(), account: Account) {
    viewModelDb.register(account)
}