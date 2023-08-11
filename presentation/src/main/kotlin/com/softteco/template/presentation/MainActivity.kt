package com.softteco.template.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.softteco.template.presentation.databinding.ActivityMainBinding
import com.softteco.template.presentation.login.LoginComposeFragmentDirections
import com.softteco.template.presentation.login.ResetPasswordComposeFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Launcher Activity (Entry point) of this application
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.data?.toString()?.contains(getString(R.string.APP_LINK_URL)) == true) {
         val token = intent.data?.toString()?.replace(getString(R.string.APP_LINK_URL),"")
            val mBundle = Bundle()
            mBundle.putString("PASSED_TOKEN",token)
            val navController = findNavController(R.id.app_nav)
            navController.navigateUp() // to clear previous navigation history
            navController.navigate(R.id.resetPasswordComposeFragment)
            navController.setGraph(navController.graph,mBundle)
        }
    }
}
