package com.misw.vinilos

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.misw.vinilos.data.network.ApiClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Prueba de conexión con la API
        lifecycleScope.launch {
            try {
                val album = ApiClient.retrofitService.getAlbum(1)
                Log.d("VINILOS_API", "ID: ${album.id}")
                Log.d("VINILOS_API", "Nombre: ${album.name}")
                Log.d("VINILOS_API", "Género: ${album.genre}")
                Log.d("VINILOS_API", "Sello: ${album.recordLabel}")
                Log.d("VINILOS_API", "Fecha: ${album.releaseDate}")
                Log.d("VINILOS_API", "Tracks: ${album.tracks.size}")
                Log.d("VINILOS_API", "Performers: ${album.performers.size}")
                Log.d("VINILOS_API", "Comments: ${album.comments.size}")
            } catch (e: Exception) {
                Log.e("VINILOS_API", "Error: ${e.message}")
            }
        }
    }
}