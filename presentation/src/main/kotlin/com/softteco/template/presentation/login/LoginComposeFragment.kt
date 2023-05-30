package com.softteco.template.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.softteco.template.presentation.login.loginComponents.MainScreenLogin
import com.softteco.template.presentation.login.loginComponents.login.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MainScreenLogin()
            }
        }
    }
}