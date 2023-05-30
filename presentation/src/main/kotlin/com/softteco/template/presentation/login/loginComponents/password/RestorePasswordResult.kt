package com.softteco.template.presentation.login.loginComponents.password

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.domain.model.user.Response
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.ProgressBar

@Composable
fun RestorePasswordResult(
    viewModel: AuthViewModel = hiltViewModel()
) {
    when (val restorePasswordResponse = viewModel.restorePasswordResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> Unit
        is Response.Failure -> print(restorePasswordResponse.e)//TODO
    }
}