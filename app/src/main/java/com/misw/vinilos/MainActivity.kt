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

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            // Actualizar el perfil activo solo al entrar al albumFragment (punto de entrada con argumento)
            if (destination.id == R.id.albumFragment) {
                val profileName = arguments?.getString("profileName") ?: ""
                if (profileName.isNotEmpty()) activeProfile = profileName
            }

            val isCollector = activeProfile == getString(R.string.collector_title)

            when (destination.id) {
                R.id.albumFragment, R.id.musicianFragment, R.id.collectorFragment -> {
                    // Coleccionista no ve el menú inferior en ninguna pantalla
                    binding.bottomNav.visibility = if (isCollector) View.GONE else View.VISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                    // Resetear perfil al volver a la pantalla de bienvenida
                    activeProfile = ""
                }
            }
        }
    }
}
