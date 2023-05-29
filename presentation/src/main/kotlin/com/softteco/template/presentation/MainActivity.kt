package com.softteco.template.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.softteco.template.presentation.databinding.ActivityMainBinding
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
    }
}
