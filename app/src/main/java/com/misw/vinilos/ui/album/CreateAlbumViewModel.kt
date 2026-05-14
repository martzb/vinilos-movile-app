package com.misw.vinilos.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.repository.AlbumRepository
import com.misw.vinilos.data.repository.MusicianRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para el formulario de creación de álbum.
 *
 * Responsabilidades:
 *  - Cargar la lista de músicos para el DropdownMenu de Artista.
 *  - Enviar el nuevo álbum al repositorio (POST /albums).
 *  - Exponer estados de carga, éxito y error.
 */
class CreateAlbumViewModel : ViewModel() {

    private val albumRepository = AlbumRepository()
    private val musicianRepository = MusicianRepository()

    // Lista de músicos para el dropdown de Artista
    private val _musicians = MutableLiveData<List<Musician>>(emptyList())
    val musicians: LiveData<List<Musician>> get() = _musicians

    // Estado de carga
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Mensaje de error (null = sin error)
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

    // Álbum creado exitosamente
    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    init {
        loadMusicians()
    }

    /**
     * Carga la lista de músicos desde el endpoint GET /musicians.
     */
    fun loadMusicians() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _musicians.value = musicianRepository.getMusicians()
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la lista de artistas."
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Envía el formulario al endpoint POST /albums.
     *
     * @param name          Nombre del álbum
     * @param musicianId    ID del músico/artista seleccionado (-1 si no se seleccionó)
     * @param releaseDate   Fecha de lanzamiento en formato ISO 8601 (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
     * @param recordLabel   Sello discográfico
     * @param genre         Género musical
     * @param description   Descripción del álbum
     */
    fun createAlbum(
        name: String,
        musicianId: Int,
        releaseDate: String,
        recordLabel: String,
        genre: String,
        description: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _isSuccess.value = false
            try {
                val album = Album(
                    id = 0,
                    name = name,
                    cover = "",
                    releaseDate = releaseDate,
                    description = description,
                    genre = genre,
                    recordLabel = recordLabel
                )
                albumRepository.createAlbum(album)
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Error al crear el álbum. Verifica tu conexión e intenta de nuevo."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }
}
