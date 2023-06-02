package com.softteco.template.presentation.login.loginComponents.password

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softteco.template.domain.model.user.ForgotPasswordDto
import com.softteco.template.presentation.R
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.CustomTopAppBar


@Composable
fun RestorePasswordScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithTopBarForgotPass(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldWithTopBarForgotPass(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    Scaffold(topBar = {
        CustomTopAppBar(navController, stringResource(id = R.string.forgot_password), true)
    }, content = {

        Column(
            modifier = Modifier.padding(20.dp, 150.dp, 20.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val email = remember { mutableStateOf(TextFieldValue()) }
            val emailErrorState = remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    if (emailErrorState.value) {
                        emailErrorState.value = false
                    }
                    email.value = it
                },
                isError = emailErrorState.value,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.email))
                },
            )
            if (emailErrorState.value) {
                Text(text = stringResource(id = R.string.required), color = Color.Red)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        when {
                            email.value.text.isEmpty() -> {
                                emailErrorState.value = true
                            }
                            else -> {
                                emailErrorState.value = false
                                authViewModel.restorePassword(
                                    ForgotPasswordDto(
                                        email.value.text,
                                    )
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(id = R.string.restore))
                }
            }
        }
    })
    RestorePasswordResult()
}