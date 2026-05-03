package com.misw.vinilos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.misw.vinilos.databinding.ActivityMainBinding

import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.albumFragment, R.id.musicianFragment, R.id.collectorFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE

                    // Ocultar "Coleccionistas" cuando el perfil activo es Coleccionista
                    val profileName = arguments?.getString("profileName") ?: ""
                    val isCollector = profileName == getString(R.string.collector_title)
                    binding.bottomNav.menu.findItem(R.id.collectorFragment)?.isVisible = !isCollector
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                    // Restaurar visibilidad al volver a la pantalla de bienvenida
                    binding.bottomNav.menu.findItem(R.id.collectorFragment)?.isVisible = true
                }
            }
        }
    }
}
