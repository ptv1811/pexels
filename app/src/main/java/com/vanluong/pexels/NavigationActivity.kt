package com.vanluong.pexels

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.vanluong.common.BaseActivity
import com.vanluong.pexels.databinding.ActivityNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point of Single-Activity Architecture.
 * Main activity that hosts the navigation graph.
 */
@AndroidEntryPoint
class NavigationActivity : BaseActivity<ActivityNavigationBinding>(R.layout.activity_navigation) {
    private val navController: NavController by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragment)?.findNavController()
            ?: findNavController(R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NavigationUI.setupWithNavController(binding.bnvFragments, navController)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clNavigation)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}