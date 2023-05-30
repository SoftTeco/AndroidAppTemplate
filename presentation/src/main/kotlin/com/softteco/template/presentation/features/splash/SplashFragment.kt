package com.softteco.template.presentation.features.splash

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.softteco.template.domain.model.Output
import com.softteco.template.presentation.R
import com.softteco.template.presentation.base.BaseFragment
import com.softteco.template.presentation.databinding.FragmentSplashBinding
import com.softteco.template.presentation.extensions.launchWhileStarted
import com.softteco.template.presentation.extensions.viewBinding
import kotlinx.coroutines.flow.collectLatest

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override val binding by viewBinding(FragmentSplashBinding::bind)
    private val splashViewModel by viewModels<SplashViewModel>()

    override fun subscribeUi() {
        viewLifecycleOwner.launchWhileStarted {
            splashViewModel.isOk.collectLatest { result ->
                if (result.status == Output.Status.SUCCESS) {
                    binding.loading.hide()
                    gotoApis()
                } else if (result.status == Output.Status.LOADING) {
                    binding.loading.show()
                }
            }
        }
    }


    /**
     * Method to navigate Fragment to API list Screen.
     */
    private fun gotoApis() = findNavController().navigate(
        SplashFragmentDirections.splashToMainNavHostFragment()
    )

    /**
     * Method to navigate Fragment to Login Screen.
     */
//    private fun gotoLogin() = findNavController().navigate(
//        SplashFragmentDirections.splashToLoginFragment()
//    )
}
