package com.softteco.template.ui.feature.login

import androidx.lifecycle.ViewModel
import com.softteco.template.data.login.LoginRepository
import com.softteco.template.data.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val loginRepository: LoginRepository,
) : ViewModel(){

}