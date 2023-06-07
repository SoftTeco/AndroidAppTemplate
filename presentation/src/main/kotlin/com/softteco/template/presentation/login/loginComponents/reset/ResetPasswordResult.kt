package com.softteco.template.presentation.login.loginComponents.reset

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.Response
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.AuthViewModelDb
import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog
import com.softteco.template.presentation.login.loginComponents.ProgressBar
import com.softteco.template.presentation.login.loginComponents.Routes

@Composable
fun ResetPasswordResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    var dialogOpen by remember {
        mutableStateOf(false)
    }
    val navController = rememberNavController()
    when (val resetPasswordResponse = viewModel.resetPasswordResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            if (dialogOpen) {
                CustomAlertDialog({
                    dialogOpen = !dialogOpen
                }, {
                    navController.navigate(Routes.Login.route)
                },
                    stringResource(id = R.string.success)) //TODO add db
            }
        }
        is Response.Failure -> {
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