package com.softteco.template.presentation.login.loginComponents.login

import androidx.compose.runtime.Composable
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun LoginUserResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when(val loginResponse = viewModel.loginResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> print(loginResponse.e)//TODO
    }
}