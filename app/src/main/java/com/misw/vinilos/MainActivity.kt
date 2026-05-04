package com.misw.vinilos

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.misw.vinilos.databinding.ActivityMainBinding

import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Perfil activo: se establece al entrar al flujo principal y persiste en todas las pantallas
    private var activeProfile: String = ""

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
        val navController = navHostFragment.navController
        
        binding.bottomNav.setupWithNavController(navController)

        // Al presionar back desde un tab secundario, vuelve a albumFragment en lugar del welcome
        val navToAlbum = NavOptions.Builder()
            .setPopUpTo(R.id.welcomeFragment, false)
            .build()

        val secondaryTabs = setOf(R.id.musicianFragment, R.id.collectorFragment)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val current = navController.currentDestination?.id
                if (current in secondaryTabs) {
                    navController.navigate(R.id.albumFragment, null, navToAlbum)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            // Actualizar el perfil activo solo al entrar al albumFragment (punto de entrada con argumento)
            if (destination.id == R.id.albumFragment) {
                val profileName = arguments?.getString("profileName") ?: ""
                if (profileName.isNotEmpty()) activeProfile = profileName
            }

            val isCollector = activeProfile == getString(R.string.collector_title)

            when (destination.id) {
                R.id.albumFragment -> {
                    binding.bottomNav.visibility = if (isCollector) View.GONE else View.VISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                    if (destination.id == R.id.welcomeFragment) activeProfile = ""
                }
            }
        }
    }
}
