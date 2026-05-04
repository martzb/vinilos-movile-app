package com.misw.vinilos

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.misw.vinilos.databinding.ActivityMainBinding

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var activeProfile: String = ""

    private val navToAlbum = lazy {
        NavOptions.Builder().setPopUpTo(R.id.welcomeFragment, false).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
        setupBackNavigation()
        setupDestinationListener()
    }

    private fun setupBackNavigation() {
        val secondaryTabs = setOf(R.id.musicianFragment, R.id.collectorFragment)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id in secondaryTabs) {
                    navController.navigate(R.id.albumFragment, null, navToAlbum.value)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }

    private fun setupDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            updateActiveProfile(destination.id, arguments)
            updateBottomNavVisibility(destination.id)
        }
    }

    private fun updateActiveProfile(destinationId: Int, arguments: Bundle?) {
        if (destinationId == R.id.albumFragment) {
            val profileName = arguments?.getString("profileName") ?: ""
            if (profileName.isNotEmpty()) activeProfile = profileName
        } else if (destinationId == R.id.welcomeFragment) {
            activeProfile = ""
        }
    }

    private fun updateBottomNavVisibility(destinationId: Int) {
        val isCollector = activeProfile == getString(R.string.collector_title)
        binding.bottomNav.visibility = if (destinationId == R.id.albumFragment && !isCollector) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
