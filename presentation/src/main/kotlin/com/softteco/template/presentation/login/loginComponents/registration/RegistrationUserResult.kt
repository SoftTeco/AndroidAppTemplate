package com.softteco.template.presentation.login.loginComponents.registration

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import androidx.hilt.navigation.compose.hiltViewModel

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.Response

import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.AuthViewModelDb
import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog

import com.softteco.template.presentation.login.loginComponents.ProgressBar


@Composable
fun RegistrationUserResult(
    viewModel: AuthViewModel = hiltViewModel(),
    account: Account,
) {
    when (val addUserResponse = viewModel.registerResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            WriteUserToDb(account = account)
            CustomAlertDialog(onGoToScreen = { /*TODO*/ }, message = "hh")
        }
        is Response.Failure -> {
            Toast.makeText(
                LocalContext.current, addUserResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun WriteUserToDb(viewModelDb: AuthViewModelDb = hiltViewModel(), account: Account) {
    viewModelDb.register(account)
}
