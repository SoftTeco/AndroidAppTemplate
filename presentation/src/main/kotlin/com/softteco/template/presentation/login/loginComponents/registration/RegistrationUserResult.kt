package com.softteco.template.presentation.login.loginComponents.registration

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDirections

import com.softteco.template.domain.model.user.Account
import com.softteco.template.domain.model.user.ApiResponse
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.*

import com.softteco.template.presentation.login.loginComponents.CustomAlertDialog

import com.softteco.template.presentation.login.loginComponents.ProgressBar


@Composable
fun RegistrationUserResult(
    viewModel: AuthViewModel = hiltViewModel(),
    account: Account,
    onNavigateToLogin: (NavDirections) -> Unit
) {

    val context = LocalContext.current
    when (val addUserResponse = viewModel.registerApiResponse) {
        is ApiResponse.Loading -> ProgressBar()
        is ApiResponse.Success -> {
            WriteUserToDb(account = account)
            CustomAlertDialog(
                onGoToScreen = {
                    onNavigateToLogin(
                        RegistrationComposeFragmentDirections
                            .actionRegistrationComposeFragmentToLoginComposeFragment()
                    )
                },
                message = context.getString(R.string.registration_success)
            )
        }
        is ApiResponse.Failure -> {
            Toast.makeText(
                context, addUserResponse.e.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun WriteUserToDb(viewModelDb: AuthViewModelDb = hiltViewModel(), account: Account) {
    viewModelDb.register(account)
}
