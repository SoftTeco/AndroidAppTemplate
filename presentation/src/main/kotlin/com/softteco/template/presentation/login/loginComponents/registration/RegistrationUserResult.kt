package com.softteco.template.presentation.login.loginComponents.registration

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.Response
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun RegistrationUserResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val addUserResponse = viewModel.registerResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> print(addUserResponse.e)//TODO
    }
}