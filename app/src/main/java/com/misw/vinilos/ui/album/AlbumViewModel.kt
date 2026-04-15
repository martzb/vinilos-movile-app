package com.misw.vinilos.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.repository.AlbumRepository
import kotlinx.coroutines.launch

class AlbumViewModel : ViewModel() {

    private val repository = AlbumRepository()

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _albumDetail = MutableLiveData<Album>()
    val albumDetail: LiveData<Album> get() = _albumDetail

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _albums.value = repository.getAlbums()
            } catch (e: Exception) {
                _error.value = "No se pudo cargar el catálogo. Verifica tu conexión."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAlbumDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _albumDetail.value = repository.getAlbumById(id)
            } catch (e: Exception) {
                _error.value = "No se pudo cargar el detalle del álbum."
            } finally {
                _isLoading.value = false
            }
        }
    }

}
