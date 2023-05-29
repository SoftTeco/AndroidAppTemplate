package com.softteco.template.presentation.features

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.softteco.template.presentation.R
import com.softteco.template.presentation.base.BaseFragment
import com.softteco.template.presentation.databinding.FragmentMainNavHostBinding
import com.softteco.template.presentation.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavHostFragment : BaseFragment(R.layout.fragment_main_nav_host) {

    override val binding by viewBinding(FragmentMainNavHostBinding::bind)
    private lateinit var navController: NavController

    override fun subscribeUi() {
        this.navController =
            (childFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment).navController
        setupBottomBarWithNavigation()
        val graph = navController.navInflater.inflate(R.navigation.main_nav)
        navController.graph = graph
    }

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
    private fun setupBottomBarWithNavigation() {
        this.navController.addOnDestinationChangedListener { _, destination, _ ->
            for (i in 0 until binding.bottomNav.menu.size()) {
                val menuItem = binding.bottomNav.menu.getItem(i)
                if (menuItem.itemId == destination.id) {
                    menuItem.isChecked = true
                }
            }
        }

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId == this.navController.currentDestination?.id) {
                return@setOnItemSelectedListener true
            }
            try {
                val options = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.destination_api, false)
                    .build()
                this.navController.navigate(menuItem.itemId, null, options, null)
                return@setOnItemSelectedListener true
            } catch (e: Exception) {
                return@setOnItemSelectedListener false
            }
        }
    }
}
