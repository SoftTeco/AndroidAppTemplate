package com.softteco.template.presentation.login.loginComponents.registration

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
import com.softteco.template.domain.model.user.CreateUserDto
import com.softteco.template.presentation.login.AuthViewModel
import com.softteco.template.presentation.login.loginComponents.CustomTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldWithTopBar(navController)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopBar(navController: NavHostController) {
    Scaffold(topBar = {
        CustomTopAppBar(navController, "Signup", true)
    }, content = {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign up", fontSize = 30.sp, color = Color.Black
            )
        }

    })
}