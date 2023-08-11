package com.softteco.template.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.softteco.template.presentation.login.loginComponents.registration.RegistrationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationComposeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RegistrationScreen(onNavigateToLogin = { dest -> findNavController().navigate(dest) })
            }
        }
    }
}