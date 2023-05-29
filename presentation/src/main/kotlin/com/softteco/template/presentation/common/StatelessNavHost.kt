package com.softteco.template.presentation.common

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment

class StatelessNavHost : NavHostFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
    }
}
