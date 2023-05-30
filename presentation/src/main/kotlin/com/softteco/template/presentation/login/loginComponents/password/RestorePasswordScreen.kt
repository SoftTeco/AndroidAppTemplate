package com.softteco.template.presentation.login.loginComponents.password

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softteco.template.domain.model.user.ForgotPasswordDto

import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = {
            CustomTopAppBar(navController, "Forgot Password", true)
        }, content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Forgot Password",
                    fontSize = 30.sp,
                    color = Color.Black
                )
            }
        })
}