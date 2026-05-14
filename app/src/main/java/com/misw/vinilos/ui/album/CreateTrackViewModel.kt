package com.misw.vinilos.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Album
import com.misw.vinilos.data.model.Track
import com.misw.vinilos.data.model.TrackRequest
import com.misw.vinilos.data.repository.AlbumRepository
import kotlinx.coroutines.launch

class CreateTrackViewModel : ViewModel() {

    private val repository = AlbumRepository()

    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album> get() = _album

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    fun fetchAlbum(albumId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val albumData = repository.getAlbumById(albumId)
                _album.value = albumData
                _tracks.value = albumData.tracks
            } catch (e: Exception) {
                _error.value = "Error al cargar los datos del álbum: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTrack(albumId: Int, name: String, duration: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _isSuccess.value = false
            try {
                val request = TrackRequest(name, duration)
                val newTrack = repository.createTrack(albumId, request)
                // Actualizar la lista local
                val currentTracks = _tracks.value?.toMutableList() ?: mutableListOf()
                currentTracks.add(newTrack)
                _tracks.value = currentTracks
                
                _isSuccess.value = true
            } catch (e: Exception) {
                _error.value = "Error al crear el track: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetSuccess() {
        _isSuccess.value = false
    }
}
